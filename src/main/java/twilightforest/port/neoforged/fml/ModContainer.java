package twilightforest.port.neoforged.fml;

import twilightforest.port.neoforged.neoforgespi.language.IModInfo;

public class ModContainer {
	private final net.fabricmc.loader.api.ModContainer container;

	public ModContainer(net.fabricmc.loader.api.ModContainer container) {
		this.container = container;
	}

	public IModInfo getModInfo() {
		return new IModInfo() {
			@Override
			public String getDisplayName() {
				return container != null ? container.getMetadata().getName() : "";
			}

			@Override
			public String getModId() {
				return container != null ? container.getMetadata().getId() : "";
			}
		};
	}
}
