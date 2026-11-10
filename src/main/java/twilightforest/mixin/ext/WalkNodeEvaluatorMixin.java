package twilightforest.mixin.ext;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.fabric.NeoHooks;

/**
 * Fabric port: IBlockExtension#getBlockPathType for path finding.
 */
@Mixin(WalkNodeEvaluator.class)
public abstract class WalkNodeEvaluatorMixin {
	@Inject(method = "getPathTypeFromState", at = @At("HEAD"), cancellable = true)
	private static void tf$blockPathType(BlockGetter level, BlockPos pos, CallbackInfoReturnable<PathType> cir) {
		BlockState state = level.getBlockState(pos);
		if (NeoHooks.overrides(state.getBlock(), "getBlockPathType", 4)) {
			PathType type = state.getBlock().getBlockPathType(state, level, pos, null);
			if (type != null) cir.setReturnValue(type);
		}
	}
}
