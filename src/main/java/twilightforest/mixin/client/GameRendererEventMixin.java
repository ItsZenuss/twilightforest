package twilightforest.mixin.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import twilightforest.port.neoforged.neoforge.client.event.RenderFrameEvent;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Fabric port: fires RenderFrameEvent.Pre/Post around each frame. */
@Mixin(GameRenderer.class)
public abstract class GameRendererEventMixin {
	@Inject(method = "render", at = @At("HEAD"))
	private void tf$framePre(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
		NeoForge.EVENT_BUS.post(new RenderFrameEvent.Pre());
	}

	@Inject(method = "render", at = @At("TAIL"))
	private void tf$framePost(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
		NeoForge.EVENT_BUS.post(new RenderFrameEvent.Post());
	}
}
