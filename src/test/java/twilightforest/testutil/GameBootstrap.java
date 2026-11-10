package twilightforest.testutil;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.Bootstrap;

/**
 * Starts Minecraft for unit tests the way the game does on Fabric. Must be loaded through Fabric's Knot class loader
 * (see {@link GameBootstrapExtension}) so it bootstraps the same Minecraft classes the tests use.
 */
public final class GameBootstrap {
	private static boolean started;

	private GameBootstrap() {}

	public static synchronized void start() {
		if (started) return;
		started = true;
		SharedConstants.tryDetectVersion();
		Bootstrap.bootStrap();
		// Fabric normally runs mod initializers from the game's main method; tests have no main, so run them here.
		boolean registered = BuiltInRegistries.ITEM.keySet().stream().anyMatch(id -> id.getNamespace().equals("twilightforest"));
		if (!registered) FabricLoader.getInstance().invokeEntrypoints("main", ModInitializer.class, ModInitializer::onInitialize);
	}
}
