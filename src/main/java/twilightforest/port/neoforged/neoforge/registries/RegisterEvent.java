package twilightforest.port.neoforged.neoforge.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class RegisterEvent {
	private final ResourceKey<? extends Registry<?>> registryKey;

	public RegisterEvent(ResourceKey<? extends Registry<?>> registryKey) {
		this.registryKey = registryKey;
	}

	public ResourceKey<? extends Registry<?>> getRegistryKey() {
		return registryKey;
	}
}
