package twilightforest.port.neoforged.neoforge.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import twilightforest.port.neoforged.bus.api.Event;

public class SubmitCustomGeometryEvent extends Event {
	private final LevelRenderState levelRenderState;
	private final PoseStack poseStack;
	private final SubmitNodeCollector submitNodeCollector;

	public SubmitCustomGeometryEvent() {
		this(null, new PoseStack(), null);
	}

	public SubmitCustomGeometryEvent(LevelRenderState levelRenderState) {
		this(levelRenderState, new PoseStack(), null);
	}

	public SubmitCustomGeometryEvent(LevelRenderState levelRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
		this.levelRenderState = levelRenderState;
		this.poseStack = poseStack;
		this.submitNodeCollector = submitNodeCollector;
	}

	public LevelRenderState getLevelRenderState() {
		return levelRenderState;
	}

	public PoseStack getPoseStack() {
		return poseStack;
	}

	public SubmitNodeCollector getSubmitNodeCollector() {
		return submitNodeCollector;
	}
}
