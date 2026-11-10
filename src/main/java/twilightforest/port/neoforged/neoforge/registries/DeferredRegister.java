package twilightforest.port.neoforged.neoforge.registries;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import twilightforest.port.neoforged.bus.api.IEventBus;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Fabric port of NeoForge's DeferredRegister.
 * <p>
 * Entries are collected when the owning class is loaded and written into the target registry when
 * {@link #register(IEventBus)} is called from the mod entrypoint. Each supplier is invoked exactly once.
 */
public class DeferredRegister<T> {
	private final ResourceKey<? extends Registry<T>> registryKey;
	private final String namespace;
	private final Map<Identifier, DeferredHolder<T, ? extends T>> entries = new LinkedHashMap<>();
	private final Map<Identifier, Identifier> aliases = new LinkedHashMap<>();
	private boolean registered = false;

	protected DeferredRegister(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
		this.registryKey = registryKey;
		this.namespace = namespace;
	}

	public static <B> DeferredRegister<B> create(ResourceKey<? extends Registry<B>> key, String namespace) {
		return new DeferredRegister<>(key, namespace);
	}

	@SuppressWarnings("unchecked")
	public static <B> DeferredRegister<B> create(Registry<B> registry, String namespace) {
		return new DeferredRegister<>((ResourceKey<? extends Registry<B>>) registry.key(), namespace);
	}

	public static DeferredRegister.Blocks createBlocks(String namespace) {
		return new DeferredRegister.Blocks(namespace);
	}

	public static DeferredRegister.Items createItems(String namespace) {
		return new DeferredRegister.Items(namespace);
	}

	public static DeferredRegister.DataComponents createDataComponents(ResourceKey<? extends Registry<DataComponentType<?>>> key, String namespace) {
		return new DeferredRegister.DataComponents(key, namespace);
	}

	@SuppressWarnings("unchecked")
	protected <I extends T, H extends DeferredHolder<T, I>> H add(H holder) {
		if (this.entries.putIfAbsent(holder.getId(), holder) != null)
			throw new IllegalArgumentException("Duplicate registration " + holder.getId());
		if (this.registered)
			this.registerEntry(this.resolveRegistry(), holder);
		return holder;
	}

	protected ResourceKey<T> keyFor(String name) {
		return ResourceKey.create(this.registryKey, Identifier.fromNamespaceAndPath(this.namespace, name));
	}

	public <I extends T> DeferredHolder<T, I> register(String name, Supplier<? extends I> supplier) {
		return this.add(new DeferredHolder<>(this.keyFor(name), supplier));
	}

	public <I extends T> DeferredHolder<T, I> register(String name, Function<Identifier, ? extends I> function) {
		Identifier id = Identifier.fromNamespaceAndPath(this.namespace, name);
		return this.register(name, () -> function.apply(id));
	}

	public void addAlias(Identifier from, Identifier to) {
		this.aliases.put(from, to);
	}

	public void register(IEventBus bus) {
		this.register();
	}

	public void register() {
		if (this.registered) return;
		this.registered = true;
		Registry<T> registry = this.resolveRegistry();
		for (DeferredHolder<T, ? extends T> holder : this.entries.values()) {
			this.registerEntry(registry, holder);
		}
		if (registry != null) {
			this.aliases.forEach((from, to) -> registry.addAlias(from, to));
		}
	}

	@Nullable
	@SuppressWarnings("unchecked")
	private Registry<T> resolveRegistry() {
		if (this.isEntityDataSerializers())
			return null;
		Registry<T> registry = (Registry<T>) BuiltInRegistries.REGISTRY.getValue(this.registryKey.identifier());
		if (registry == null) {
			// NeoForge-provided registries (loot modifier serializers, condition codecs...) don't exist on Fabric; create them.
			registry = FabricRegistryBuilder.create((ResourceKey<Registry<T>>) this.registryKey).buildAndRegister();
		}
		return registry;
	}

	private boolean isEntityDataSerializers() {
		return this.registryKey.identifier().equals(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS.identifier());
	}

	private <I extends T> void registerEntry(@Nullable Registry<T> registry, DeferredHolder<T, I> holder) {
		I value = holder.createValue();
		if (this.isEntityDataSerializers()) {
			FabricEntityDataRegistry.register(holder.getId(), (EntityDataSerializer<?>) value);
		} else if (registry != null) {
			Registry.register(registry, holder.getKey(), value);
		}
		if (this.registryKey.identifier().equals(NeoForgeRegistries.Keys.CONDITION_CODECS.identifier())) {
			twilightforest.port.neoforged.neoforge.common.conditions.FabricConditionBridge.register(holder.getId(), (com.mojang.serialization.MapCodec<? extends twilightforest.port.neoforged.neoforge.common.conditions.ICondition>) value);
		}
		if (value instanceof twilightforest.port.neoforged.neoforge.attachment.AttachmentType<?> attachment) {
			attachment.registerFabric(holder.getId());
		}
		holder.bind(value);
	}

	public Collection<DeferredHolder<T, ? extends T>> getEntries() {
		return Collections.unmodifiableCollection(this.entries.values());
	}

	public ResourceKey<? extends Registry<T>> getRegistryKey() {
		return this.registryKey;
	}

	public Identifier getRegistryName() {
		return this.registryKey.identifier();
	}

	public String getNamespace() {
		return this.namespace;
	}

	public static class Blocks extends DeferredRegister<Block> {
		public Blocks(String namespace) {
			super(Registries.BLOCK, namespace);
		}

		public <B extends Block> DeferredBlock<B> registerBlock(String name, Function<BlockBehaviour.Properties, B> func, Supplier<BlockBehaviour.Properties> props) {
			ResourceKey<Block> key = this.keyFor(name);
			return this.add(new DeferredBlock<>(key, () -> func.apply(props.get().setId(key))));
		}

		public <B extends Block> DeferredBlock<B> registerBlock(String name, Function<BlockBehaviour.Properties, B> func) {
			return this.registerBlock(name, func, BlockBehaviour.Properties::of);
		}

		public DeferredBlock<Block> registerSimpleBlock(String name, Supplier<BlockBehaviour.Properties> props) {
			return this.registerBlock(name, Block::new, props);
		}

		@Override
		public <B extends Block> DeferredBlock<B> register(String name, Supplier<? extends B> sup) {
			return this.add(new DeferredBlock<>(this.keyFor(name), sup));
		}

		@Override
		public <B extends Block> DeferredBlock<B> register(String name, Function<Identifier, ? extends B> func) {
			ResourceKey<Block> key = this.keyFor(name);
			return this.add(new DeferredBlock<>(key, () -> func.apply(key.identifier())));
		}
	}

	public static class Items extends DeferredRegister<Item> {
		public Items(String namespace) {
			super(Registries.ITEM, namespace);
		}

		public <I extends Item> DeferredItem<I> registerItem(String name, Function<Item.Properties, I> func, Supplier<Item.Properties> props) {
			ResourceKey<Item> key = this.keyFor(name);
			return this.add(new DeferredItem<>(key, () -> func.apply(props.get().setId(key))));
		}

		public <I extends Item> DeferredItem<I> registerItem(String name, Function<Item.Properties, I> func) {
			return this.registerItem(name, func, Item.Properties::new);
		}

		public DeferredItem<Item> registerSimpleItem(String name, Supplier<Item.Properties> props) {
			return this.registerItem(name, Item::new, props);
		}

		public DeferredItem<Item> registerSimpleItem(String name) {
			return this.registerItem(name, Item::new, Item.Properties::new);
		}

		@Override
		public <I extends Item> DeferredItem<I> register(String name, Supplier<? extends I> sup) {
			return this.add(new DeferredItem<>(this.keyFor(name), sup));
		}

		@Override
		public <I extends Item> DeferredItem<I> register(String name, Function<Identifier, ? extends I> func) {
			ResourceKey<Item> key = this.keyFor(name);
			return this.add(new DeferredItem<>(key, () -> func.apply(key.identifier())));
		}
	}

	public static class DataComponents extends DeferredRegister<DataComponentType<?>> {
		public DataComponents(String namespace) {
			super(Registries.DATA_COMPONENT_TYPE, namespace);
		}

		public DataComponents(ResourceKey<? extends Registry<DataComponentType<?>>> key, String namespace) {
			super(key, namespace);
		}

		public <C> DeferredHolder<DataComponentType<?>, DataComponentType<C>> registerComponentType(String name, java.util.function.UnaryOperator<DataComponentType.Builder<C>> builderFunction) {
			return this.register(name, () -> builderFunction.apply(DataComponentType.builder()).build());
		}
	}
}
