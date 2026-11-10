package twilightforest.port.neoforged.neoforge.client;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.state.level.WeatherRenderState;
import net.minecraft.world.phys.Vec3;

public interface CustomWeatherEffectRenderer {
	boolean renderSnowAndRain(LevelRenderState levelRenderState, WeatherRenderState weatherRenderState, MultiBufferSource bufferSource, Vec3 camPos);
	boolean tickRain(ClientLevel level, int ticks, Camera camera);
}
