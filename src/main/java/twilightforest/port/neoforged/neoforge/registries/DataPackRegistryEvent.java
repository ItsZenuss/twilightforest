package twilightforest.port.neoforged.neoforge.registries;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class DataPackRegistryEvent {

	public static class NewRegistry {
		public <T> void dataPackRegistry(ResourceKey<Registry<T>> key, Codec<T> codec) {
			DynamicRegistries.register(key, codec);
		}

		public <T> void dataPackRegistry(ResourceKey<Registry<T>> key, Codec<T> codec, Codec<T> networkCodec) {
			DynamicRegistries.registerSynced(key, codec, networkCodec);
		}
	}
}
