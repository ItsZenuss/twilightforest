package twilightforest.port.neoforged.fml;

import net.fabricmc.loader.api.FabricLoader;

import java.util.Optional;

public final class ModList {
	private static final ModList INSTANCE = new ModList();

	private ModList() {}

	public static ModList get() {
		return INSTANCE;
	}

	public boolean isLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}

	public Optional<ModContainer> getModContainerById(String modId) {
		return FabricLoader.getInstance().getModContainer(modId).map(ModContainer::new);
	}
}
