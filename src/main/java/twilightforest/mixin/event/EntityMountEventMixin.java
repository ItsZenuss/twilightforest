package twilightforest.mixin.event;

import net.minecraft.world.entity.Entity;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.entity.EntityMountEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Fabric port: fires EntityMountEvent on mounting and dismounting. */
@Mixin(Entity.class)
public abstract class EntityMountEventMixin {
	@Shadow
	public abstract Entity getVehicle();

	@Inject(method = "startRiding(Lnet/minecraft/world/entity/Entity;ZZ)Z", at = @At("HEAD"), cancellable = true)
	private void tf$mount(Entity vehicle, boolean force, boolean sendEvent, CallbackInfoReturnable<Boolean> cir) {
		Entity self = (Entity) (Object) this;
		if (NeoForge.EVENT_BUS.post(new EntityMountEvent(self, vehicle, self.level(), true)).isCanceled()) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "removeVehicle", at = @At("HEAD"), cancellable = true)
	private void tf$dismount(CallbackInfo ci) {
		Entity self = (Entity) (Object) this;
		Entity vehicle = this.getVehicle();
		if (vehicle != null && !vehicle.isRemoved() && self.isAlive()
			&& NeoForge.EVENT_BUS.post(new EntityMountEvent(self, vehicle, self.level(), false)).isCanceled()) {
			ci.cancel();
		}
	}
}
