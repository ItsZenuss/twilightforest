package twilightforest.port.neoforged.neoforge.registries;

import net.minecraft.core.Registry;

public class NewRegistryEvent {
	public <T> void register(Registry<T> registry) {
		// Handled directly via FabricRegistryBuilder.buildAndRegister()
	}
}
