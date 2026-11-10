package twilightforest.mixin.ext;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.fabric.NeoHooks;

/**
 * Fabric port: IBlockExtension#canConnectRedstone.
 */
@Mixin(RedStoneWireBlock.class)
public abstract class RedStoneWireBlockMixin {
	@Inject(method = "shouldConnectTo(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z", at = @At("HEAD"), cancellable = true)
	private static void tf$canConnectRedstone(BlockState state, Direction direction, CallbackInfoReturnable<Boolean> cir) {
		if (NeoHooks.overrides(state.getBlock(), "canConnectRedstone", 4)) {
			cir.setReturnValue(state.getBlock().canConnectRedstone(state, null, null, direction));
		}
	}
}
