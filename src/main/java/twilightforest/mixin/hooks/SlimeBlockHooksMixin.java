package twilightforest.mixin.hooks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asmhooks.BlockHooks;

/**
 * Fabric port of the NeoForge coremod transformer {@code twilightforest.asm.transformers.block.SlimeBlockBounceUpTransformer, block.SlimeBlockMomentumTransformer}.
 */
@Mixin(SlimeBlock.class)
public abstract class SlimeBlockHooksMixin {
	@Inject(method = "bounceUp", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getDeltaMovement()Lnet/minecraft/world/phys/Vec3;", ordinal = 0))
	private void tf$stopBouncing(Entity entity, CallbackInfo ci) {
		BlockHooks.stopBouncing(entity);
	}

	@ModifyExpressionValue(method = "stepOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isSteppingCarefully()Z"))
	private boolean tf$momentum(boolean original, Level level, BlockPos pos, BlockState state, Entity entity) {
		return BlockHooks.resetSlimeMomentumWithUnrestrained(original, entity);
	}
}
