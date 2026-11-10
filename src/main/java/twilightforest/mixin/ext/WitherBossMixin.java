package twilightforest.mixin.ext;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.NeoHooks;

/**
 * Fabric port: IBlockExtension#canEntityDestroy for the wither.
 */
@Mixin(WitherBoss.class)
public abstract class WitherBossMixin {
	@WrapOperation(method = "customServerAiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/boss/wither/WitherBoss;canDestroy(Lnet/minecraft/world/level/block/state/BlockState;)Z"))
	private boolean tf$canEntityDestroy(BlockState state, Operation<Boolean> original, @Local(argsOnly = true) ServerLevel level, @Local BlockPos pos) {
		if (NeoHooks.overrides(state.getBlock(), "canEntityDestroy", 4) && !state.getBlock().canEntityDestroy(state, level, pos, (WitherBoss) (Object) this)) {
			return false;
		}
		return original.call(state);
	}
}
