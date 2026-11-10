package twilightforest.port.neoforged.neoforge.client.model;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class DelegateBlockStateModel implements BlockStateModel {
	protected final BlockStateModel original;

	public DelegateBlockStateModel(BlockStateModel original) {
		this.original = original;
	}

	@Override
	public void collectParts(RandomSource random, List<BlockStateModelPart> parts) {
		if (original != null) {
			original.collectParts(random, parts);
		}
	}

	@Override
	public Material.Baked particleMaterial() {
		return original != null ? original.particleMaterial() : null;
	}

	@Override
	public int materialFlags() {
		return original != null ? original.materialFlags() : 0;
	}

	public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
		collectParts(random, parts);
	}

	public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return particleMaterial();
	}

	public int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return materialFlags();
	}

	@Override
	public void emitQuads(net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter emitter, net.minecraft.client.renderer.block.BlockAndTintGetter level,
						  net.minecraft.core.BlockPos pos, net.minecraft.world.level.block.state.BlockState state, net.minecraft.util.RandomSource random,
						  java.util.function.Predicate<net.minecraft.core.Direction> cullTest) {
		this.original.emitQuads(emitter, level, pos, state, random, cullTest);
	}
}
