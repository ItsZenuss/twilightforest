package twilightforest.port.neoforged.neoforge.client.event;

import net.minecraft.client.player.LocalPlayer;
import twilightforest.port.neoforged.bus.api.Event;

public class ClientPlayerNetworkEvent extends Event {
	private final LocalPlayer player;

	public ClientPlayerNetworkEvent(LocalPlayer player) {
		this.player = player;
	}

	public LocalPlayer getPlayer() {
		return player;
	}

	public static class LoggingOut extends ClientPlayerNetworkEvent {
		public LoggingOut(LocalPlayer player) {
			super(player);
		}
	}

	public static class LoggingIn extends ClientPlayerNetworkEvent {
		public LoggingIn(LocalPlayer player) {
			super(player);
		}
	}
}
