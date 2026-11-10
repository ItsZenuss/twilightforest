package twilightforest.port.neoforged.neoforge.client;

import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import org.joml.Matrix4fc;

public interface CustomSkyboxRenderer {
	boolean renderSky(LevelRenderState levelRenderState, SkyRenderState skyRenderState, Matrix4fc modelViewMatrix, Runnable setupFog);
}
