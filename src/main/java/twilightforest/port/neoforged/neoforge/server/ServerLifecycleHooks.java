package twilightforest.port.neoforged.neoforge.server;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Fabric port of NeoForge's ServerLifecycleHooks. {@link #init()} must be called from the main entrypoint.
 */
public final class ServerLifecycleHooks {
	@Nullable
	private static MinecraftServer currentServer;
	private static Supplier<@Nullable RegistryAccess> clientRegistryAccess = () -> null;
	private static boolean initialized;

	private ServerLifecycleHooks() {}

	public static void init() {
		if (initialized) return;
		initialized = true;
		ServerLifecycleEvents.SERVER_STARTING.register(server -> currentServer = server);
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			if (currentServer == server) {
				currentServer = null;
			}
		});
	}

	/**
	 * Called by the client entrypoint so common code can reach the client's registries without loading client classes.
	 */
	public static void setClientRegistryAccess(Supplier<@Nullable RegistryAccess> supplier) {
		clientRegistryAccess = supplier;
	}

	@Nullable
	public static MinecraftServer getCurrentServer() {
		return currentServer;
	}

	public static void setCurrentServer(@Nullable MinecraftServer server) {
		currentServer = server;
	}

	/**
	 * @return the running server's registries, or the client's when only a client world is loaded
	 */
	@Nullable
	public static RegistryAccess getCurrentRegistryAccess() {
		if (currentServer != null)
			return currentServer.registryAccess();
		return clientRegistryAccess.get();
	}
}
