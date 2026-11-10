package twilightforest.mixin.client;

import net.minecraft.client.renderer.debug.DebugRenderer;
import twilightforest.port.neoforged.neoforge.client.event.RegisterDebugRenderersEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Fabric port: includes debug renderers registered through NeoForge's RegisterDebugRenderersEvent (e.g. Twilight
 * Forest's multipart hitboxes shown with F3+B).
 */
@Mixin(DebugRenderer.class)
public abstract class DebugRendererMixin {
	@Shadow
	@Final
	private List<DebugRenderer.SimpleDebugRenderer> renderers;

	@Inject(method = "refreshRendererList", at = @At("TAIL"))
	private void tf$addRenderers(CallbackInfo ci) {
		this.renderers.addAll(RegisterDebugRenderersEvent.getRenderers());
	}
}
