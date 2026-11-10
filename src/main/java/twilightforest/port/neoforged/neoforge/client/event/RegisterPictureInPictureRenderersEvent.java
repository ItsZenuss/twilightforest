package twilightforest.port.neoforged.neoforge.client.event;

import net.fabricmc.fabric.api.client.rendering.v1.PictureInPictureRendererRegistry;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import twilightforest.port.neoforged.bus.api.Event;

import java.util.function.Function;

public class RegisterPictureInPictureRenderersEvent extends Event {
	public <T, R> void register(Class<T> stateClass, Function<MultiBufferSource.BufferSource, R> rendererFactory) {
		PictureInPictureRendererRegistry.register(context -> (PictureInPictureRenderer<?>) rendererFactory.apply(context.bufferSource()));
	}
}
