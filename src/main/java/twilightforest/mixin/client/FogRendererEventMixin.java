package twilightforest.mixin.client;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogRenderer;
import twilightforest.port.neoforged.neoforge.client.event.ViewportEvent;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Fabric port: fires ViewportEvent.ComputeFogColor after vanilla computes the fog colour. */
@Mixin(FogRenderer.class)
public abstract class FogRendererEventMixin {
	@Inject(method = "computeFogColor", at = @At("TAIL"))
	private void tf$fogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenAmount, Vector4f color, CallbackInfo ci) {
		ViewportEvent.ComputeFogColor event = NeoForge.EVENT_BUS.post(new ViewportEvent.ComputeFogColor(camera, color.x, color.y, color.z));
		color.set(event.getRed(), event.getGreen(), event.getBlue(), color.w);
	}
}
