package twilightforest.port.neoforged.neoforge.client.model;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface DynamicBlockStateModel extends BlockStateModel {
	@Override
	default void collectParts(RandomSource random, List<BlockStateModelPart> parts) {
	}

	@Override
	default int materialFlags() {
		return 0;
	}

	default void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
		collectParts(random, parts);
	}

	default Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return particleMaterial();
	}

	default int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return materialFlags();
	}

	/**
	 * Fabric port: Fabric's renderer asks models for quads through emitQuads (with the level and position), so route
	 * that to the level-aware collectParts, as NeoForge's renderer did.
	 */
	@Override
	default void emitQuads(net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter emitter, BlockAndTintGetter level, BlockPos pos, BlockState state,
						   RandomSource random, java.util.function.Predicate<net.minecraft.core.Direction> cullTest) {
		List<BlockStateModelPart> parts = new java.util.ArrayList<>();
		this.collectParts(level, pos, state, random, parts);
		for (BlockStateModelPart part : parts) {
			part.emitQuads(emitter, cullTest);
		}
	}

	@Override
	default Object createGeometryKey(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
		return null; // geometry depends on the surroundings; never cache
	}
}
