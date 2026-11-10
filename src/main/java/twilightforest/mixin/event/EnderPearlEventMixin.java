package twilightforest.mixin.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.phys.HitResult;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.entity.EntityTeleportEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Fabric port: fires EntityTeleportEvent for ender pearl teleports. */
@Mixin(ThrownEnderpearl.class)
public abstract class EnderPearlEventMixin {
	@Inject(method = "onHit", at = @At("HEAD"), cancellable = true)
	private void tf$pearlTeleport(HitResult hit, CallbackInfo ci) {
		ThrownEnderpearl self = (ThrownEnderpearl) (Object) this;
		Entity owner = self.getOwner();
		if (owner != null && !self.level().isClientSide()) {
			if (NeoForge.EVENT_BUS.post(new EntityTeleportEvent(owner, hit.getLocation().x(), hit.getLocation().y(), hit.getLocation().z())).isCanceled()) {
				self.discard();
				ci.cancel();
			}
		}
	}
}
