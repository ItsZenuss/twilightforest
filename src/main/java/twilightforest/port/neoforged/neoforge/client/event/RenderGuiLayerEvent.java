package twilightforest.port.neoforged.neoforge.client.event;

import net.minecraft.resources.Identifier;
import twilightforest.port.neoforged.bus.api.Event;

public class RenderGuiLayerEvent extends Event {
	public static class Pre extends RenderGuiLayerEvent {
		private boolean canceled;
		private final Identifier name;

		public Pre(Identifier name) {
			this.name = name;
		}

		public Identifier getName() {
			return name;
		}

		public boolean isCanceled() {
			return canceled;
		}

		public void setCanceled(boolean canceled) {
			this.canceled = canceled;
		}
	}
}
