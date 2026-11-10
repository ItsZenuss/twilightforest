package twilightforest.port.neoforged.neoforge.client.event;

import net.minecraft.client.input.KeyEvent;
import twilightforest.port.neoforged.bus.api.Event;

public class InputEvent extends Event {
	public static class Key extends InputEvent {
		private final KeyEvent keyEvent;
		private final int action;

		public Key(KeyEvent keyEvent, int action) {
			this.keyEvent = keyEvent;
			this.action = action;
		}

		public KeyEvent getKeyEvent() {
			return keyEvent;
		}

		public int getAction() {
			return action;
		}
	}
}
