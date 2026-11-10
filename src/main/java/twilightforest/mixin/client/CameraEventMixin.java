package twilightforest.mixin.client;

import net.minecraft.client.Camera;
import twilightforest.port.neoforged.neoforge.client.event.ViewportEvent;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Fabric port: fires ViewportEvent.ComputeCameraAngles after the camera aligns with its entity (supports roll). */
@Mixin(Camera.class)
public abstract class CameraEventMixin {
	@Shadow
	public abstract float yRot();

	@Shadow
	public abstract float xRot();

	@Shadow
	protected abstract void setRotation(float yRot, float xRot);

	@Shadow
	@Final
	private Quaternionf rotation;

	@Inject(method = "alignWithEntity", at = @At("TAIL"))
	private void tf$cameraAngles(float partialTick, CallbackInfo ci) {
		ViewportEvent.ComputeCameraAngles event = NeoForge.EVENT_BUS.post(new ViewportEvent.ComputeCameraAngles((Camera) (Object) this, this.yRot(), this.xRot(), 0.0F, partialTick));
		if (event.getYaw() != this.yRot() || event.getPitch() != this.xRot()) {
			this.setRotation(event.getYaw(), event.getPitch());
		}
		if (event.getRoll() != 0.0F) {
			this.rotation.rotateZ((float) Math.toRadians(event.getRoll()));
		}
	}
}
