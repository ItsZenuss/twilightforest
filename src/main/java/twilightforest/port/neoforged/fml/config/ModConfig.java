package twilightforest.port.neoforged.fml.config;

import twilightforest.port.neoforged.neoforge.common.ModConfigSpec;

public class ModConfig {
	public enum Type {
		COMMON,
		CLIENT,
		SERVER
	}

	private final Type type;
	private final ModConfigSpec spec;

	public ModConfig(Type type, ModConfigSpec spec) {
		this.type = type;
		this.spec = spec;
	}

	public Type getType() {
		return type;
	}

	public ModConfigSpec getSpec() {
		return spec;
	}
}
