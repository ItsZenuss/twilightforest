package twilightforest.fabric;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;

/**
 * Implemented on SimpleJsonResourceReloadListener by a mixin; exposes the (registry-aware, for Twilight Forest
 * listeners) ops used to decode JSON.
 */
public interface RegistryAwareJsonOps {
	DynamicOps<JsonElement> twilightforest$getOps();
}
