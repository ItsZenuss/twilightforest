package twilightforest.port.neoforged.neoforge.client.model.standalone;

import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.fabricmc.fabric.api.client.model.loading.v1.UnbakedExtraModel;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import net.minecraft.resources.Identifier;

/**
 * Fabric port: standalone models become Fabric extra models.
 */
public interface SimpleUnbakedStandaloneModel {
	UnbakedExtraModel<?> toFabric();

	static SimpleUnbakedStandaloneModel quadCollection(Identifier location) {
		return () -> new SimpleUnbakedExtraModel<>(location, (resolved, baker) ->
			resolved.bakeTopGeometry(resolved.getTopTextureSlots(), baker, BlockModelRotation.IDENTITY));
	}

	static SimpleUnbakedStandaloneModel simpleModelWrapper(Identifier location) {
		return () -> new SimpleUnbakedExtraModel<>(location, (resolved, baker) ->
			SimpleModelWrapper.bake(baker, location, BlockModelRotation.IDENTITY));
	}
}
