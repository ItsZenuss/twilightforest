package twilightforest.port.neoforged.fml.event.config;

import twilightforest.port.neoforged.fml.config.ModConfig;

public class ModConfigEvent {
	private final ModConfig config;

	public ModConfigEvent(ModConfig config) {
		this.config = config;
	}

	public ModConfig getConfig() {
		return config;
	}

	public static class Loading extends ModConfigEvent {
		public Loading(ModConfig config) {
			super(config);
		}
	}

	public static class Reloading extends ModConfigEvent {
		public Reloading(ModConfig config) {
			super(config);
		}
	}
}
