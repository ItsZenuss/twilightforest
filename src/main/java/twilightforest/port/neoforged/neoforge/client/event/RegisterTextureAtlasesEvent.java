package twilightforest.port.neoforged.neoforge.client.event;

import net.fabricmc.fabric.api.client.rendering.v1.AtlasRegistry;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import twilightforest.port.neoforged.bus.api.Event;

public class RegisterTextureAtlasesEvent extends Event {
	public void register(AtlasManager.AtlasConfig config) {
		AtlasRegistry.register(config);
	}
}
