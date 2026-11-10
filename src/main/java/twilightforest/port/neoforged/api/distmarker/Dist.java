package twilightforest.port.neoforged.api.distmarker;

import net.fabricmc.api.EnvType;

public enum Dist {
	CLIENT(EnvType.CLIENT),
	DEDICATED_SERVER(EnvType.SERVER);

	private final EnvType envType;

	Dist(EnvType envType) {
		this.envType = envType;
	}

	public EnvType toEnvType() {
		return envType;
	}

	public boolean isClient() {
		return this == CLIENT;
	}

	public boolean isDedicatedServer() {
		return this == DEDICATED_SERVER;
	}
}
