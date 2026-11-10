package twilightforest.port.neoforged.neoforge.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import twilightforest.port.neoforged.bus.api.Event;

public class RenderLevelStageEvent extends Event {
	public static class AfterWeather extends RenderLevelStageEvent {
		private final LevelRenderState levelRenderState;
		private final PoseStack poseStack;

		public AfterWeather() {
			this(new LevelRenderState(), new PoseStack());
		}

		public AfterWeather(LevelRenderState levelRenderState) {
			this(levelRenderState, new PoseStack());
		}

		public AfterWeather(LevelRenderState levelRenderState, PoseStack poseStack) {
			this.levelRenderState = levelRenderState;
			this.poseStack = poseStack;
		}

		public LevelRenderState getLevelRenderState() {
			return levelRenderState;
		}

		public PoseStack getPoseStack() {
			return poseStack;
		}
	}
}
