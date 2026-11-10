package twilightforest.mixin.client;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.fabric.ClientRenderBridge;

/**
 * Fabric port: captures the frame's culling frustum for NeoForge's ExtractLevelRenderStateEvent#getFrustum.
 */
@Mixin(LevelRenderer.class)
public abstract class LevelRendererFrustumMixin {
	@Inject(method = "extractVisibleEntities", at = @At("HEAD"))
	private void tf$captureFrustum(Camera camera, Frustum frustum, DeltaTracker deltaTracker, LevelRenderState state, CallbackInfo ci) {
		ClientRenderBridge.currentFrustum = frustum;
	}
}
