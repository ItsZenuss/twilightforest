package twilightforest.port.neoforged.neoforge.registries.datamaps;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataMapType<R, T> {
	private final Identifier id;
	private final ResourceKey<? extends Registry<R>> registryKey;
	private final Codec<T> codec;
	private final Map<ResourceKey<R>, T> data = new ConcurrentHashMap<>();
	private final Map<net.minecraft.tags.TagKey<R>, T> tagData = new ConcurrentHashMap<>();

	public DataMapType(Identifier id, ResourceKey<? extends Registry<R>> registryKey, Codec<T> codec) {
		this.id = id;
		this.registryKey = registryKey;
		this.codec = codec;
	}

	public Identifier getId() {
		return id;
	}

	public ResourceKey<? extends Registry<R>> getRegistryKey() {
		return registryKey;
	}

	public Codec<T> getCodec() {
		return codec;
	}

	public void put(ResourceKey<R> key, T value) {
		data.put(key, value);
	}

	public T get(ResourceKey<R> key) {
		return data.get(key);
	}

	public void putTag(net.minecraft.tags.TagKey<R> tag, T value) {
		tagData.put(tag, value);
	}

	public Map<net.minecraft.tags.TagKey<R>, T> getTagData() {
		return tagData;
	}

	public void clear() {
		data.clear();
		tagData.clear();
	}

	public T get(Holder<R> holder) {
		if (holder.unwrapKey().isPresent()) {
			T direct = data.get(holder.unwrapKey().get());
			if (direct != null) return direct;
		}
		for (Map.Entry<net.minecraft.tags.TagKey<R>, T> entry : tagData.entrySet()) {
			if (holder.is(entry.getKey())) return entry.getValue();
		}
		return null;
	}

	public Map<ResourceKey<R>, T> getData() {
		return data;
	}

	public static <R, T> Builder<R, T> builder(Identifier id, ResourceKey<? extends Registry<R>> registryKey, Codec<T> codec) {
		return new Builder<>(id, registryKey, codec);
	}

	public static class Builder<R, T> {
		private final Identifier id;
		private final ResourceKey<? extends Registry<R>> registryKey;
		private final Codec<T> codec;

		public Builder(Identifier id, ResourceKey<? extends Registry<R>> registryKey, Codec<T> codec) {
			this.id = id;
			this.registryKey = registryKey;
			this.codec = codec;
		}

		public Builder<R, T> synced(Codec<T> networkCodec, boolean mandatory) {
			return this;
		}

		public DataMapType<R, T> build() {
			return new DataMapType<>(id, registryKey, codec);
		}
	}
}
