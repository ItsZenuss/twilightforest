package twilightforest.port.neoforged.neoforge.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import twilightforest.port.neoforged.bus.api.Event;

public class ExtractBlockOutlineRenderStateEvent extends Event {
	private boolean canceled;
	private final BlockPos blockPos;
	private final Camera camera;

	public ExtractBlockOutlineRenderStateEvent(BlockPos blockPos, Camera camera) {
		this.blockPos = blockPos;
		this.camera = camera;
	}

	public BlockPos getBlockPos() {
		return blockPos;
	}

	public Camera getCamera() {
		return camera;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	private final java.util.List<CustomOutlineRenderer> customRenderers = new java.util.ArrayList<>();

	public void addCustomRenderer(CustomOutlineRenderer renderer) {
		this.customRenderers.add(renderer);
	}

	public java.util.List<CustomOutlineRenderer> getCustomRenderers() {
		return this.customRenderers;
	}

	@FunctionalInterface
	public interface CustomOutlineRenderer {
		boolean render(net.minecraft.client.renderer.state.level.BlockOutlineRenderState renderState, MultiBufferSource.BufferSource buffer, PoseStack poseStack, boolean translucentPass, LevelRenderState levelRenderState);
	}
}
