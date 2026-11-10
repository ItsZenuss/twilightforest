package twilightforest.port.neoforged.fml;

import twilightforest.port.neoforged.fml.config.ModConfig;
import twilightforest.port.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.Supplier;

public final class ModLoadingContext {
	private static final ModLoadingContext INSTANCE = new ModLoadingContext();
	private final ModContainer container = new ModContainer();

	private ModLoadingContext() {}

	public static ModLoadingContext get() {
		return INSTANCE;
	}

	public ModContainer getActiveContainer() {
		return container;
	}

	public <T> void registerExtensionPoint(Class<T> type, Supplier<T> supplier) {
		// Extension points (e.g. config screen)
	}

	public static class ModContainer {
		private final java.util.List<ModConfig> configs = new java.util.ArrayList<>();

		public void registerConfig(ModConfig.Type type, ModConfigSpec spec) {
			this.configs.add(new ModConfig(type, spec));
		}

		public java.util.List<ModConfig> getConfigs() {
			return java.util.List.copyOf(this.configs);
		}
	}
}
