package twilightforest.mixin.client;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.client.renderer.state.level.WeatherRenderState;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.world.phys.Vec3;
import twilightforest.port.neoforged.neoforge.client.CustomWeatherEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.client.fabric.ClientRenderBridge;

/**
 * Fabric port: lets the Twilight Forest's custom weather renderer replace vanilla rain/snow and rain particles
 * (NeoForge custom_weather_effects).
 */
@Mixin(WeatherEffectRenderer.class)
public abstract class WeatherEffectRendererMixin {
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void tf$customWeather(Vec3 cameraPos, WeatherRenderState state, CallbackInfo ci) {
		CustomWeatherEffectRenderer renderer = ClientRenderBridge.weatherRenderer();
		if (renderer != null && ClientRenderBridge.lastLevelState != null
			&& renderer.renderSnowAndRain(ClientRenderBridge.lastLevelState, state, Minecraft.getInstance().renderBuffers().bufferSource(), cameraPos)) {
			ci.cancel();
		}
	}

	@Inject(method = "tickRainParticles", at = @At("HEAD"), cancellable = true)
	private void tf$customRainTick(ClientLevel level, Camera camera, int ticks, ParticleStatus particles, int rainDistance, CallbackInfo ci) {
		CustomWeatherEffectRenderer renderer = ClientRenderBridge.weatherRenderer();
		if (renderer != null && renderer.tickRain(level, ticks, camera)) {
			ci.cancel();
		}
	}
}
