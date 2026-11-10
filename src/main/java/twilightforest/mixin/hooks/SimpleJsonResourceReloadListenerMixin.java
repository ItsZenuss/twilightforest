package twilightforest.mixin.hooks;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import twilightforest.fabric.RegistryAwareJsonOps;

/**
 * Fabric port: NeoForge hands JSON reload listeners registry-aware ops; vanilla's plain constructor uses
 * {@link JsonOps}. For Twilight Forest's listeners, swap in registry ops from the reload's shared state so codecs
 * such as Ingredient (which resolve item ids) work.
 */
@Mixin(SimpleJsonResourceReloadListener.class)
public abstract class SimpleJsonResourceReloadListenerMixin implements PreparableReloadListener, RegistryAwareJsonOps {
	@Shadow
	@Final
	@Mutable
	private DynamicOps<JsonElement> ops;

	@Override
	public void prepareSharedState(PreparableReloadListener.SharedState state) {
		if (this.getClass().getName().startsWith("twilightforest.") && this.ops == JsonOps.INSTANCE) {
			HolderLookup.Provider registries = state.get(ResourceLoader.REGISTRY_LOOKUP_KEY);
			if (registries != null) {
				this.ops = registries.createSerializationContext(JsonOps.INSTANCE);
			}
		}
	}

	@Override
	public DynamicOps<JsonElement> twilightforest$getOps() {
		return this.ops;
	}
}
