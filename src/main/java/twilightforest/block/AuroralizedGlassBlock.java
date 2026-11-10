package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.util.ColorUtil;
import twilightforest.util.SimplexNoiseHelper;

/**
 * Fabric port: implements vanilla's BeaconBeamBlock so beacons treat it as a tinting block; the actual colour comes
 * from {@link #getBeaconColorMultiplier} via {@code twilightforest.mixin.ext.BeaconBlockEntityMixin}.
 */
public class AuroralizedGlassBlock extends TransparentBlock implements net.minecraft.world.level.block.BeaconBeamBlock {

	public AuroralizedGlassBlock(Properties properties) {
		super(properties);
	}

	public Integer getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos) {
		return ColorUtil.hsvToRGB(SimplexNoiseHelper.rippleFractalNoise(2, 128.0f, pos.above(128), 0.37f, 0.67f, 1.5f), 1.0f, 1.0f);
	}

	@Override
	public net.minecraft.world.item.DyeColor getColor() {
		return net.minecraft.world.item.DyeColor.WHITE;
	}
}
