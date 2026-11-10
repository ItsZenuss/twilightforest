package twilightforest.mixin.client;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import twilightforest.port.neoforged.neoforge.client.CustomSkyboxRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.fabric.ClientRenderBridge;

/**
 * Fabric port: lets the Twilight Forest's custom skybox renderer replace vanilla's sky (NeoForge custom_skybox).
 */
@Mixin(LevelRenderer.class)
public abstract class SkyAndWeatherMixin {
	@Inject(method = "lambda$addSkyPass$0", at = @At("HEAD"), cancellable = true)
	private static void tf$customSky(GpuBufferSlice fog, SkyRenderState skyState, SkyRenderer skyRenderer, CallbackInfo ci) {
		CustomSkyboxRenderer renderer = ClientRenderBridge.skyRenderer();
		if (renderer != null && ClientRenderBridge.lastLevelState != null
			&& renderer.renderSky(ClientRenderBridge.lastLevelState, skyState, RenderSystem.getModelViewMatrix(), () -> RenderSystem.setShaderFog(fog))) {
			ci.cancel();
		}
	}
}
