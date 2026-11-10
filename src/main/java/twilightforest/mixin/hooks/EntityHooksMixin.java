package twilightforest.mixin.hooks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asmhooks.EntityHooks;

/**
 * Fabric port of the NeoForge coremod transformer {@code twilightforest.asm.transformers.entity.UnrestrainedBlockSpeedAndJumpFactorTransformer, entity.ResetStuckUnrestrainedTransformer}.
 */
@Mixin(Entity.class)
public abstract class EntityHooksMixin {
	@ModifyReturnValue(method = {"getBlockJumpFactor", "getBlockSpeedFactor"}, at = @At("RETURN"))
	private float tf$unrestrainedFactor(float original) {
		return EntityHooks.resetFactorWithUnrestrained(original, (Entity) (Object) this);
	}

	@Inject(method = "move", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/Entity;stuckSpeedMultiplier:Lnet/minecraft/world/phys/Vec3;", opcode = org.objectweb.asm.Opcodes.GETFIELD, ordinal = 0))
	private void tf$resetStuck(MoverType type, Vec3 movement, CallbackInfo ci) {
		EntityHooks.resetStuckUnrestrained((Entity) (Object) this);
	}
}
