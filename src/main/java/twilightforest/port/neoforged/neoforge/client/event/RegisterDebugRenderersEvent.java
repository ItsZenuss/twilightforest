package twilightforest.port.neoforged.neoforge.client.event;

import net.minecraft.client.renderer.debug.DebugRenderer;
import twilightforest.port.neoforged.bus.api.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Fabric port: extra debug renderers, added to vanilla's list by {@code twilightforest.mixin.client.DebugRendererMixin}.
 */
public class RegisterDebugRenderersEvent extends Event {
	private static final List<DebugRenderer.SimpleDebugRenderer> RENDERERS = new ArrayList<>();

	public void register(DebugRenderer.SimpleDebugRenderer renderer) {
		RENDERERS.add(renderer);
	}

	public static List<DebugRenderer.SimpleDebugRenderer> getRenderers() {
		return RENDERERS;
	}
}
