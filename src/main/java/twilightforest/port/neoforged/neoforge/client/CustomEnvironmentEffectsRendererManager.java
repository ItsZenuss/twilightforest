package twilightforest.port.neoforged.neoforge.client;

import net.minecraft.resources.Identifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomEnvironmentEffectsRendererManager {
	private static final Map<Identifier, CustomWeatherEffectRenderer> weatherRenderers = new ConcurrentHashMap<>();
	private static final Map<Identifier, CustomSkyboxRenderer> skyboxRenderers = new ConcurrentHashMap<>();

	public static void register(Identifier id, CustomWeatherEffectRenderer renderer) {
		weatherRenderers.put(id, renderer);
	}

	public static void register(Identifier id, CustomSkyboxRenderer renderer) {
		skyboxRenderers.put(id, renderer);
	}

	public static CustomWeatherEffectRenderer getCustomWeatherEffectRenderer(Identifier id) {
		return weatherRenderers.get(id);
	}

	public static CustomSkyboxRenderer getCustomSkyboxRenderer(Identifier id) {
		return skyboxRenderers.get(id);
	}
}
