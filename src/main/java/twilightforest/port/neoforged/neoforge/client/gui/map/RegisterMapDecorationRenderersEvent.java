package twilightforest.port.neoforged.neoforge.client.gui.map;

import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import twilightforest.port.neoforged.bus.api.Event;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Fabric port: custom map decoration renderers, consulted by {@code twilightforest.mixin.client.MapRendererMixin}.
 */
public class RegisterMapDecorationRenderersEvent extends Event {
	private static final Map<MapDecorationType, IMapDecorationRenderer> RENDERERS = new IdentityHashMap<>();

	public void register(MapDecorationType type, Object renderer) {
		RENDERERS.put(type, (IMapDecorationRenderer) renderer);
	}

	@Nullable
	public static IMapDecorationRenderer get(MapDecorationType type) {
		return RENDERERS.get(type);
	}
}
