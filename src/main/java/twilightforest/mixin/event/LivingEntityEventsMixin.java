package twilightforest.mixin.event;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.common.damagesource.DamageContainer;
import twilightforest.port.neoforged.neoforge.event.entity.living.LivingEvent;
import twilightforest.port.neoforged.neoforge.event.entity.living.LivingFallEvent;
import twilightforest.port.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import twilightforest.port.neoforged.neoforge.event.entity.living.MobEffectEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Fabric port: fires NeoForge living-entity events that Fabric API has no equivalent for.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityEventsMixin {

	@WrapMethod(method = "hurtServer")
	private boolean tf$fireIncomingDamage(ServerLevel level, DamageSource source, float amount, Operation<Boolean> original) {
		LivingEntity self = (LivingEntity) (Object) this;
		LivingIncomingDamageEvent event = NeoForge.EVENT_BUS.post(new LivingIncomingDamageEvent(self, new DamageContainer(source, amount)));
		if (event.isCanceled()) return false;
		return original.call(level, source, event.getAmount());
	}

	@WrapMethod(method = "causeFallDamage")
	private boolean tf$fireFall(double fallDistance, float multiplier, DamageSource source, Operation<Boolean> original) {
		LivingFallEvent event = NeoForge.EVENT_BUS.post(new LivingFallEvent((LivingEntity) (Object) this, (float) fallDistance, multiplier));
		if (event.isCanceled()) return false;
		return original.call((double) event.getDistance(), event.getDamageMultiplier(), source);
	}

	@Inject(method = "jumpFromGround", at = @At("TAIL"))
	private void tf$fireJump(CallbackInfo ci) {
		NeoForge.EVENT_BUS.post(new LivingEvent.LivingJumpEvent((LivingEntity) (Object) this));
	}

	@Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
	private void tf$fireEffectApplicable(MobEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
		MobEffectEvent.Applicable event = NeoForge.EVENT_BUS.post(new MobEffectEvent.Applicable((LivingEntity) (Object) this, effect));
		if (event.getResult() == MobEffectEvent.Applicable.Result.DO_NOT_APPLY) cir.setReturnValue(false);
	}
}
