package twilightforest.mixin.ext;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.NeoHooks;

/**
 * Fabric port: IBlockExtension#onDestroyedByPlayer.
 */
@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin {
	@Shadow
	@Final
	protected ServerPlayer player;

	@WrapOperation(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"))
	private boolean tf$onDestroyedByPlayer(ServerLevel level, BlockPos pos, boolean moved, Operation<Boolean> original) {
		BlockState state = level.getBlockState(pos);
		if (NeoHooks.overrides(state.getBlock(), "onDestroyedByPlayer", 7)) {
			return state.getBlock().onDestroyedByPlayer(state, level, pos, this.player, this.player.getMainHandItem(), this.player.hasCorrectToolForDrops(state), level.getFluidState(pos));
		}
		return original.call(level, pos, moved);
	}
}
