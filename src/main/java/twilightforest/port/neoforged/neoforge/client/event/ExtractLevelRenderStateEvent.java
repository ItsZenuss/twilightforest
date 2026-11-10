package twilightforest.port.neoforged.neoforge.client.event;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import twilightforest.port.neoforged.bus.api.Event;

/**
 * Fabric port: fired from Fabric's LevelRenderEvents.END_EXTRACTION.
 */
public class ExtractLevelRenderStateEvent extends Event {
	private final LevelRenderState renderState;
	private final ClientLevel level;
	private final Camera camera;
	private final DeltaTracker deltaTracker;
	private final Frustum frustum;
	private final LevelRenderer levelRenderer;

	public ExtractLevelRenderStateEvent(LevelRenderState renderState, ClientLevel level, Camera camera, DeltaTracker deltaTracker, Frustum frustum, LevelRenderer levelRenderer) {
		this.renderState = renderState;
		this.level = level;
		this.camera = camera;
		this.deltaTracker = deltaTracker;
		this.frustum = frustum;
		this.levelRenderer = levelRenderer;
	}

	public LevelRenderState getRenderState() {
		return renderState;
	}

	public LevelRenderState getLevelRenderState() {
		return renderState;
	}

	public ClientLevel getLevel() {
		return level;
	}

	public DeltaTracker getDeltaTracker() {
		return deltaTracker;
	}

	public Frustum getFrustum() {
		return frustum;
	}

	public Camera getCamera() {
		return camera;
	}

	public LevelRenderer getLevelRenderer() {
		return levelRenderer;
	}

	public int getRenderTick() {
		return levelRenderer.ticks;
	}
}
