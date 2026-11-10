package twilightforest.port.neoforged.neoforge.client.model.standalone;

import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.resources.Identifier;

/**
 * Fabric port: wraps a Fabric {@link ExtraModelKey}.
 */
public class StandaloneModelKey<T> {
	private final ExtraModelKey<T> fabricKey;

	public StandaloneModelKey(Identifier location) {
		this.fabricKey = ExtraModelKey.create(location::toString);
	}

	public StandaloneModelKey(ModelDebugName debugName) {
		this.fabricKey = ExtraModelKey.create(debugName::debugName);
	}

	public ExtraModelKey<T> fabricKey() {
		return this.fabricKey;
	}
}
