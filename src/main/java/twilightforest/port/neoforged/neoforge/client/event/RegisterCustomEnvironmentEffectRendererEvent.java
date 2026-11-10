package twilightforest.port.neoforged.neoforge.client.event;

import net.minecraft.resources.Identifier;
import twilightforest.port.neoforged.bus.api.Event;
import twilightforest.port.neoforged.neoforge.client.CustomEnvironmentEffectsRendererManager;
import twilightforest.port.neoforged.neoforge.client.CustomSkyboxRenderer;
import twilightforest.port.neoforged.neoforge.client.CustomWeatherEffectRenderer;

public class RegisterCustomEnvironmentEffectRendererEvent extends Event {
	public void registerSkyboxRenderer(Identifier id, Object renderer) {
		CustomEnvironmentEffectsRendererManager.register(id, (CustomSkyboxRenderer) renderer);
	}

	public void registerWeatherEffectRenderer(Identifier id, Object renderer) {
		CustomEnvironmentEffectsRendererManager.register(id, (CustomWeatherEffectRenderer) renderer);
	}
}
