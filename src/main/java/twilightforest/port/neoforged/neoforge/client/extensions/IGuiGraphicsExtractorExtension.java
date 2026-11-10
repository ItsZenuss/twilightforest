package twilightforest.port.neoforged.neoforge.client.extensions;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import org.jspecify.annotations.Nullable;

public interface IGuiGraphicsExtractorExtension {
	default void submitPictureInPictureRenderState(PictureInPictureRenderState state) {
	}

	@Nullable
	default ScreenRectangle peekScissorStack() {
		return null;
	}
}
