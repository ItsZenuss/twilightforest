package twilightforest.port.neoforged.neoforge.client.event;

import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import twilightforest.port.neoforged.bus.api.Event;

public class RegisterKeyMappingsEvent extends Event {
	public void register(KeyMapping keyMapping) {
		KeyMappingHelper.registerKeyMapping(keyMapping);
	}
}
