package twilightforest.port.neoforged.fml.loading;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import twilightforest.port.neoforged.api.distmarker.Dist;

/**
 * Fabric port. The NeoForge shims live under {@code twilightforest.port.neoforged} rather than NeoForge's real
 * {@code net.neoforged} packages, because multi-loader libraries (e.g. Yumi, bundled by Trinkets) probe for NeoForge
 * classes and would otherwise apply their NeoForge mixins to these shims.
 */
public class FMLEnvironment {
	public static boolean isProduction() {
		return !FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	public static Dist getDist() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? Dist.CLIENT : Dist.DEDICATED_SERVER;
	}
}
