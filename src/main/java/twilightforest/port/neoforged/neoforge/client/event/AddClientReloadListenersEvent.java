package twilightforest.port.neoforged.neoforge.client.event;

import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import twilightforest.port.neoforged.bus.api.Event;

public class AddClientReloadListenersEvent extends Event {
	public void addListener(Identifier id, PreparableReloadListener listener) {
		ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(id, listener);
	}
}
