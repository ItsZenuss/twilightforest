package twilightforest.port.neoforged.neoforge.event;

import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public class AddServerReloadListenersEvent {
	public void addListener(Identifier id, PreparableReloadListener listener) {
		ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(id, listener);
	}
}
