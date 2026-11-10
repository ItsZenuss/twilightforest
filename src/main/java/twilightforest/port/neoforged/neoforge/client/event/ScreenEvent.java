package twilightforest.port.neoforged.neoforge.client.event;

import net.minecraft.client.gui.screens.Screen;
import twilightforest.port.neoforged.bus.api.Event;

public class ScreenEvent extends Event {
	private final Screen screen;

	public ScreenEvent(Screen screen) {
		this.screen = screen;
	}

	public Screen getScreen() {
		return screen;
	}

	public static class Init extends ScreenEvent {
		public Init(Screen screen) {
			super(screen);
		}

		public static class Post extends Init {
			public Post(Screen screen) {
				super(screen);
			}
		}
	}

	public static class Closing extends ScreenEvent {
		public Closing(Screen screen) {
			super(screen);
		}
	}
}
