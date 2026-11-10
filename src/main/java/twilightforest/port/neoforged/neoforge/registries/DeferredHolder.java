package twilightforest.port.neoforged.neoforge.registries;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import twilightforest.port.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Fabric port of NeoForge's DeferredHolder.
 * <p>
 * Once its entry is registered, the holder delegates to the registry's own {@link Holder.Reference}, so tags,
 * equality checks and serialization behave like they do for vanilla holders. Holders created with
 * {@link #create(ResourceKey, Identifier)} for datapack registries resolve through the running server (or client).
 */
public class DeferredHolder<R, T extends R> implements Supplier<T>, Holder<R> {
	private final ResourceKey<R> key;
	@Nullable
	private final Supplier<? extends T> supplier;
	@Nullable
	private T value;
	@Nullable
	private Holder<R> delegate;

	public DeferredHolder(ResourceKey<R> key, @Nullable Supplier<? extends T> supplier) {
		this.key = Objects.requireNonNull(key);
		this.supplier = supplier;
	}

	protected DeferredHolder(ResourceKey<R> key) {
		this(key, null);
	}

	public static <R, T extends R> DeferredHolder<R, T> create(ResourceKey<R> key) {
		return new DeferredHolder<>(key);
	}

	public static <R, T extends R> DeferredHolder<R, T> create(ResourceKey<? extends Registry<R>> registryKey, Identifier id) {
		return create(ResourceKey.create(registryKey, id));
	}

	/**
	 * Creates the entry's value. Only {@link DeferredRegister} should call this, exactly once.
	 */
	T createValue() {
		if (this.supplier == null)
			throw new IllegalStateException("No supplier for registry object " + this.key);
		return this.supplier.get();
	}

	@Nullable
	@SuppressWarnings("unchecked")
	private Holder<R> resolveDelegate() {
		if (this.delegate != null)
			return this.delegate;
		Registry<R> registry = (Registry<R>) BuiltInRegistries.REGISTRY.getValue(this.key.registry());
		if (registry != null) {
			this.delegate = registry.get(this.key).orElse(null);
			return this.delegate;
		}
		// Datapack registry: look it up from whichever side is running, without caching (it changes per world).
		RegistryAccess access = ServerLifecycleHooks.getCurrentRegistryAccess();
		if (access != null) {
			return access.lookup(this.key.registryKey()).flatMap(r -> r.get(this.key)).orElse(null);
		}
		return null;
	}

	@Nullable
	public Holder<R> getDelegate() {
		return this.resolveDelegate();
	}

	@Override
	@SuppressWarnings("unchecked")
	public T get() {
		if (this.value != null)
			return this.value;
		Holder<R> holder = this.resolveDelegate();
		if (holder == null || !holder.isBound())
			throw new IllegalStateException("Registry object not present: " + this.key);
		T resolved = (T) holder.value();
		if (this.delegate != null)
			this.value = resolved; // static registries never change, cache them
		return resolved;
	}

	@Override
	public T value() {
		return this.get();
	}

	public ResourceKey<R> getKey() {
		return this.key;
	}

	public Identifier getId() {
		return this.key.identifier();
	}

	public Holder<R> asHolder() {
		Holder<R> holder = this.resolveDelegate();
		return holder != null ? holder : Holder.direct(this.get());
	}

	@Override
	public boolean isBound() {
		Holder<R> holder = this.resolveDelegate();
		return this.value != null || (holder != null && holder.isBound());
	}

	@Override
	public boolean areComponentsBound() {
		Holder<R> holder = this.resolveDelegate();
		return holder == null || holder.areComponentsBound();
	}

	@Override
	public boolean is(Identifier id) {
		return this.getId().equals(id);
	}

	@Override
	public boolean is(ResourceKey<R> key) {
		return this.key.equals(key);
	}

	@Override
	public boolean is(Predicate<ResourceKey<R>> predicate) {
		return predicate.test(this.key);
	}

	@Override
	public boolean is(TagKey<R> tag) {
		Holder<R> holder = this.resolveDelegate();
		return holder != null && holder.is(tag);
	}

	@Override
	public boolean is(Holder<R> holder) {
		return holder.unwrapKey().map(this.key::equals).orElse(false) || this.asHolder().is(holder);
	}

	@Override
	public Stream<TagKey<R>> tags() {
		Holder<R> holder = this.resolveDelegate();
		return holder != null ? holder.tags() : Stream.empty();
	}

	@Override
	public DataComponentMap components() {
		return this.asHolder().components();
	}

	@Override
	public Either<ResourceKey<R>, R> unwrap() {
		return Either.left(this.key);
	}

	@Override
	public Optional<ResourceKey<R>> unwrapKey() {
		return Optional.of(this.key);
	}

	@Override
	public Kind kind() {
		return Kind.REFERENCE;
	}

	@Override
	public boolean canSerializeIn(HolderOwner<R> owner) {
		Holder<R> holder = this.resolveDelegate();
		return holder != null && holder.canSerializeIn(owner);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		return obj instanceof Holder<?> h && h.unwrapKey().map(this.key::equals).orElse(false);
	}

	@Override
	public int hashCode() {
		return this.key.hashCode();
	}

	@Override
	public String toString() {
		return "DeferredHolder{" + this.key + "}";
	}

	void bind(T val) {
		this.value = val;
	}
}
