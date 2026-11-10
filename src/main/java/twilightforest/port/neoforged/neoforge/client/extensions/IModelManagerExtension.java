package twilightforest.port.neoforged.neoforge.client.extensions;

import net.fabricmc.fabric.api.client.model.loading.v1.FabricModelManager;
import twilightforest.port.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

/**
 * Fabric port: standalone models are looked up through Fabric's model manager extension.
 */
public interface IModelManagerExtension {
	default <T> T getStandaloneModel(StandaloneModelKey<T> key) {
		return ((FabricModelManager) this).getModel(key.fabricKey());
	}
}
