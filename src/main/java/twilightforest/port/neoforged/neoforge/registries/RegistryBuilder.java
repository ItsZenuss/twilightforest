package twilightforest.port.neoforged.neoforge.registries;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class RegistryBuilder<T> {
	private final ResourceKey<Registry<T>> key;
	private boolean sync = false;

	public RegistryBuilder(ResourceKey<Registry<T>> key) {
		this.key = key;
	}

	public RegistryBuilder<T> sync(boolean sync) {
		this.sync = sync;
		return this;
	}

	public Registry<T> create() {
		FabricRegistryBuilder<T, ?> builder = FabricRegistryBuilder.create(key);
		return builder.buildAndRegister();
	}
}
