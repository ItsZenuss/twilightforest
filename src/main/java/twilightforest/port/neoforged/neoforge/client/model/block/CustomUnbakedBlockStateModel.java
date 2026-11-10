package twilightforest.port.neoforged.neoforge.client.model.block;

import com.mojang.serialization.MapCodec;

/**
 * Fabric port: Twilight Forest's custom block state models are Fabric custom unbaked block state models
 * (selected with "fabric:type" in blockstate JSON).
 */
public interface CustomUnbakedBlockStateModel extends net.fabricmc.fabric.api.client.model.loading.v1.CustomUnbakedBlockStateModel {
	@Override
	MapCodec<? extends CustomUnbakedBlockStateModel> codec();
}
