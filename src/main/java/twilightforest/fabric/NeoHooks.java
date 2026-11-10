package twilightforest.fabric;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Fabric port helper for NeoForge-style extension methods.
 * <p>
 * NeoForge patched vanilla to call methods like {@code IBlockExtension#getBlockPathType} on every block. On Fabric
 * the hook mixins only route to those methods when the object's class actually overrides them, so vanilla blocks,
 * items and entities keep their exact vanilla behaviour.
 */
public final class NeoHooks {
	private record Key(Class<?> type, String name, int params) {}

	private static final Map<Key, Boolean> CACHE = new ConcurrentHashMap<>();

	private NeoHooks() {}

	/**
	 * @return true if {@code obj}'s class (or a superclass other than a NeoForge shim interface default) declares a
	 * method named {@code name} with {@code params} parameters.
	 */
	public static boolean overrides(Object obj, String name, int params) {
		if (obj == null) return false;
		return CACHE.computeIfAbsent(new Key(obj.getClass(), name, params), NeoHooks::scan);
	}

	private static boolean scan(Key key) {
		for (Class<?> c = key.type(); c != null && c != Object.class; c = c.getSuperclass()) {
			if (c.getName().startsWith("net.minecraft.")) return false;
			for (Method m : c.getDeclaredMethods()) {
				if (m.getName().equals(key.name()) && m.getParameterCount() == key.params() && !m.isBridge() && !m.isSynthetic()) {
					return true;
				}
			}
		}
		return false;
	}
}
