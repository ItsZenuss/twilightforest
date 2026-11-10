package twilightforest.mixin.ext;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.port.neoforged.neoforge.common.util.TriState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.NeoHooks;

/**
 * Fabric port: IBlockExtension#canSustainPlant for plants.
 */
@Mixin(VegetationBlock.class)
public abstract class VegetationBlockMixin {
	@ModifyReturnValue(method = "canSurvive", at = @At("RETURN"))
	private boolean tf$canSustainPlant(boolean original, BlockState state, LevelReader level, BlockPos pos) {
		BlockState soil = level.getBlockState(pos.below());
		if (NeoHooks.overrides(soil.getBlock(), "canSustainPlant", 5)) {
			TriState result = soil.getBlock().canSustainPlant(soil, level, pos.below(), Direction.UP, state);
			if (!result.isDefault()) return result.isTrue();
		}
		return original;
	}
}
