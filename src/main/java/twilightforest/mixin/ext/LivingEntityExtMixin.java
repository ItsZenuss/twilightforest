package twilightforest.mixin.ext;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.fabric.NeoHooks;

/**
 * Fabric port: IBlockExtension#addLandingEffects and #isScaffolding.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityExtMixin {
	@WrapOperation(method = "checkFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"))
	private int tf$landingEffects(ServerLevel level, ParticleOptions particle, double x, double y, double z, int count, double dx, double dy, double dz, double speed, Operation<Integer> original,
								  @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) BlockPos pos) {
		if (NeoHooks.overrides(state.getBlock(), "addLandingEffects", 6)
			&& state.getBlock().addLandingEffects(state, level, pos, state, (LivingEntity) (Object) this, count)) {
			return 0;
		}
		return original.call(level, particle, x, y, z, count, dx, dy, dz, speed);
	}

	@WrapOperation(method = "handleOnClimbable", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"))
	private boolean tf$isScaffolding(BlockState state, Object block, Operation<Boolean> original) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (NeoHooks.overrides(state.getBlock(), "isScaffolding", 4) && state.getBlock().isScaffolding(state, self.level(), self.blockPosition(), self)) {
			return true;
		}
		return original.call(state, block);
	}
}
