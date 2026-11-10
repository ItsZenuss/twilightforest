package twilightforest.port.neoforged.neoforge.client.event;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.BossEvent;
import twilightforest.port.neoforged.bus.api.Event;

public class CustomizeGuiOverlayEvent extends Event {
	public static class BossEventProgress extends CustomizeGuiOverlayEvent {
		private boolean canceled;
		private final BossEvent bossEvent;
		private final GuiGraphicsExtractor guiGraphics;
		private final int x;
		private final int y;

		public BossEventProgress(BossEvent bossEvent, GuiGraphicsExtractor guiGraphics, int x, int y) {
			this.bossEvent = bossEvent;
			this.guiGraphics = guiGraphics;
			this.x = x;
			this.y = y;
		}

		public BossEvent getBossEvent() {
			return bossEvent;
		}

		public GuiGraphicsExtractor getGuiGraphics() {
			return guiGraphics;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public boolean isCanceled() {
			return canceled;
		}

		public void setCanceled(boolean canceled) {
			this.canceled = canceled;
		}
	}
}
