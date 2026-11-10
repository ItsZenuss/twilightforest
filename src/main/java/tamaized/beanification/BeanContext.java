package tamaized.beanification;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import twilightforest.port.neoforged.api.distmarker.Dist;
import twilightforest.port.neoforged.bus.api.IEventBus;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Fabric re-implementation of the parts of Tamaized's "beanification" container Twilight Forest uses.
 * <p>
 * {@link #init(String)} scans the mod's classes with ASM (so client-only classes are never loaded on a dedicated
 * server). {@link #start()} then fills static {@link Autowired} fields and constructs every {@link Component}
 * that applies to this side, injecting fields and running {@link PostConstruct} methods.
 */
public final class BeanContext {
	private static final Logger LOGGER = LoggerFactory.getLogger("twilightforest-beans");
	private static final String COMPONENT = Type.getDescriptor(Component.class);
	private static final String AUTOWIRED = Type.getDescriptor(Autowired.class);
	private static final String CONFIGURABLE = Type.getDescriptor(Configurable.class);
	private static final String CLIENT_PACKAGE = "twilightforest.client.";
	private static final String COMPAT_PACKAGE = "twilightforest.compat.";
	/** Compat sub-packages and the mod each one needs. */
	private static final Map<String, String> COMPAT_MODS = Map.of("jei", "jei", "jade", "jade", "trinkets", "trinkets", "emi", "emi", "rei", "roughlyenoughitems");

	private static final Map<Class<?>, Object> BEANS = new ConcurrentHashMap<>();
	private static final Set<String> COMPONENT_CLASSES = new LinkedHashSet<>();
	private static final Set<String> STATIC_INJECT_CLASSES = new LinkedHashSet<>();
	private static final Set<String> CONFIGURABLE_CLASSES = new LinkedHashSet<>();
	private static IEventBus modBus;
	private static boolean scanned;
	private static boolean started;

	private BeanContext() {}

	public static void setModBus(IEventBus bus) {
		modBus = bus;
	}

	public static IEventBus getModBus() {
		return modBus;
	}

	public static synchronized void init(String modId) {
		if (scanned) return;
		scanned = true;
		ModContainer container = FabricLoader.getInstance().getModContainer(modId).orElseThrow();
		List<Path> bases = new ArrayList<>();
		for (Path root : container.getRootPaths()) {
			Path base = root.resolve(modId);
			if (Files.isDirectory(base)) bases.add(base);
		}
		boolean hasClasses = bases.stream().anyMatch(b -> Files.exists(b.resolve("TwilightForestMod.class")));
		if (!hasClasses) {
			// In a dev environment the mod's root paths may only cover resources; find the compiled classes directly.
			try {
				Path codeSource = Path.of(BeanContext.class.getProtectionDomain().getCodeSource().getLocation().toURI());
				if (Files.isDirectory(codeSource.resolve(modId))) bases.add(codeSource.resolve(modId));
			} catch (Exception e) {
				LOGGER.error("Could not locate mod classes for bean scanning", e);
			}
		}
		for (Path base : bases) {
			try (Stream<Path> files = Files.walk(base)) {
				files.filter(p -> p.toString().endsWith(".class")).forEach(BeanContext::scan);
			} catch (IOException e) {
				LOGGER.error("Failed scanning {} for beans", base, e);
			}
		}
		LOGGER.info("Found {} components, {} classes with static injection", COMPONENT_CLASSES.size(), STATIC_INJECT_CLASSES.size());
	}

	private static void scan(Path file) {
		try (InputStream in = Files.newInputStream(file)) {
			ClassReader reader = new ClassReader(in);
			String name = reader.getClassName().replace('/', '.');
			boolean[] component = {false};
			List<String> dists = new ArrayList<>();
			boolean[] staticInject = {false};
			boolean[] configurable = {false};
			reader.accept(new ClassVisitor(Opcodes.ASM9) {
				@Override
				public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
					if (COMPONENT.equals(descriptor)) {
						component[0] = true;
						return new AnnotationVisitor(Opcodes.ASM9) {
							@Override
							public AnnotationVisitor visitArray(String n) {
								return new AnnotationVisitor(Opcodes.ASM9) {
									@Override
									public void visitEnum(String n2, String desc, String value) {
										dists.add(value);
									}
								};
							}
						};
					}
					if (CONFIGURABLE.equals(descriptor)) configurable[0] = true;
					return null;
				}

				@Override
				public FieldVisitor visitField(int access, String n, String descriptor, String signature, Object value) {
					if ((access & Opcodes.ACC_STATIC) == 0) return null;
					return new FieldVisitor(Opcodes.ASM9) {
						@Override
						public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
							if (AUTOWIRED.equals(desc)) staticInject[0] = true;
							return null;
						}
					};
				}
			}, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

			boolean client = isClient();
			boolean sideOk = dists.isEmpty() || dists.contains(client ? "CLIENT" : "DEDICATED_SERVER");
			boolean clientOnlyPackage = (!client && name.startsWith(CLIENT_PACKAGE)) || missingCompatMod(name);
			if (component[0] && sideOk && !clientOnlyPackage) COMPONENT_CLASSES.add(name);
			if (staticInject[0] && !clientOnlyPackage) STATIC_INJECT_CLASSES.add(name);
			if (configurable[0]) CONFIGURABLE_CLASSES.add(name);
		} catch (IOException e) {
			LOGGER.error("Failed reading {}", file, e);
		}
	}

	/**
	 * Injects static fields and constructs all components. Call once from the main entrypoint, before any registry
	 * suppliers run, so classes like TFItems see their injected fields.
	 */
	public static synchronized void start() {
		if (started) return;
		started = true;
		ClassLoader loader = BeanContext.class.getClassLoader();
		for (String name : STATIC_INJECT_CLASSES) {
			try {
				injectStatics(Class.forName(name, false, loader));
			} catch (Throwable t) {
				LOGGER.error("Failed static injection into {}", name, t);
			}
		}
		for (String name : COMPONENT_CLASSES) {
			try {
				get(Class.forName(name, true, loader));
			} catch (Throwable t) {
				LOGGER.error("Failed to create component {}", name, t);
			}
		}
	}

	public static <T> T register(T bean) {
		if (bean != null) {
			BEANS.put(bean.getClass(), bean);
			inject(bean);
		}
		return bean;
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> type) {
		T existing = (T) BEANS.get(type);
		if (existing != null) {
			return existing;
		}

		for (Map.Entry<Class<?>, Object> entry : BEANS.entrySet()) {
			if (type.isAssignableFrom(entry.getKey())) {
				return (T) entry.getValue();
			}
		}

		if (type.isInterface() || Modifier.isAbstract(type.getModifiers())) {
			return null;
		}

		try {
			Constructor<T> ctor = type.getDeclaredConstructor();
			ctor.setAccessible(true);
			T instance = ctor.newInstance();
			BEANS.put(type, instance);
			inject(instance);
			invokePostConstruct(instance);
			return instance;
		} catch (ReflectiveOperationException e) {
			LOGGER.error("Could not construct bean {}", type.getName(), e);
			return null;
		}
	}

	private static void injectStatics(Class<?> clazz) throws IllegalAccessException {
		for (Field field : clazz.getDeclaredFields()) {
			if (!Modifier.isStatic(field.getModifiers()) || !field.isAnnotationPresent(Autowired.class)) continue;
			Autowired autowired = field.getAnnotation(Autowired.class);
			if (!shouldLoad(autowired.dist(), autowired.requiresModIdsLoaded())) continue;
			field.setAccessible(true);
			if (field.get(null) == null) {
				Object bean = get(field.getType());
				if (bean != null) field.set(null, bean);
			}
		}
	}

	public static void inject(Object instance) {
		if (instance == null) return;
		Class<?> clazz = instance.getClass();
		while (clazz != null && clazz != Object.class) {
			for (Field field : clazz.getDeclaredFields()) {
				if (Modifier.isStatic(field.getModifiers()) || !field.isAnnotationPresent(Autowired.class)) continue;
				Autowired autowired = field.getAnnotation(Autowired.class);
				if (!shouldLoad(autowired.dist(), autowired.requiresModIdsLoaded())) continue;
				try {
					field.setAccessible(true);
					if (field.get(instance) == null) {
						Object bean = get(field.getType());
						if (bean != null) field.set(instance, bean);
					}
				} catch (ReflectiveOperationException e) {
					LOGGER.error("Failed injecting {}.{}", clazz.getName(), field.getName(), e);
				}
			}
			clazz = clazz.getSuperclass();
		}
	}

	public static void invokePostConstruct(Object instance) {
		if (instance == null) return;
		Class<?> clazz = instance.getClass();
		while (clazz != null && clazz != Object.class) {
			for (Method method : clazz.getDeclaredMethods()) {
				if (!method.isAnnotationPresent(PostConstruct.class)) continue;
				try {
					method.setAccessible(true);
					if (method.getParameterCount() == 1 && method.getParameterTypes()[0].isAssignableFrom(IEventBus.class)) {
						PostConstruct pc = method.getAnnotation(PostConstruct.class);
						IEventBus targetBus = (pc.value() == PostConstruct.Bus.GAME) ? NeoForge.EVENT_BUS : modBus;
						method.invoke(instance, targetBus);
					} else if (method.getParameterCount() == 0) {
						method.invoke(instance);
					}
				} catch (ReflectiveOperationException e) {
					LOGGER.error("PostConstruct failed for {}.{}", clazz.getName(), method.getName(), e);
				}
			}
			clazz = clazz.getSuperclass();
		}
	}

	private static boolean missingCompatMod(String className) {
		if (!className.startsWith(COMPAT_PACKAGE)) return false;
		String rest = className.substring(COMPAT_PACKAGE.length());
		int dot = rest.indexOf('.');
		if (dot < 0) return false;
		String modId = COMPAT_MODS.get(rest.substring(0, dot));
		return modId != null && !FabricLoader.getInstance().isModLoaded(modId);
	}

	private static boolean isClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}

	private static boolean shouldLoad(Dist[] dists, String[] requiresModIds) {
		if (dists != null && dists.length > 0) {
			boolean client = isClient();
			boolean matches = false;
			for (Dist d : dists) {
				if (d == Dist.CLIENT && client) matches = true;
				if (d == Dist.DEDICATED_SERVER && !client) matches = true;
			}
			if (!matches) return false;
		}
		if (requiresModIds != null) {
			for (String modId : requiresModIds) {
				if (!FabricLoader.getInstance().isModLoaded(modId)) return false;
			}
		}
		return true;
	}
}
