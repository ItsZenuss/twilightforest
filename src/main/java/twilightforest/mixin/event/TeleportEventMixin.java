package twilightforest.mixin.event;

import net.minecraft.world.entity.LivingEntity;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.entity.EntityTeleportEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Fabric port: fires EntityTeleportEvent for random teleports (chorus fruit, endermen...). */
@Mixin(LivingEntity.class)
public abstract class TeleportEventMixin {
	@Inject(method = "randomTeleport", at = @At("HEAD"), cancellable = true)
	private void tf$randomTeleport(double x, double y, double z, boolean particles, CallbackInfoReturnable<Boolean> cir) {
		if (NeoForge.EVENT_BUS.post(new EntityTeleportEvent((LivingEntity) (Object) this, x, y, z)).isCanceled()) {
			cir.setReturnValue(false);
		}
	}
}
