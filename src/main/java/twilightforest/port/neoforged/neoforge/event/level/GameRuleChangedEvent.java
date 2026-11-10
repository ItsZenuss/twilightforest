package twilightforest.port.neoforged.neoforge.event.level;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.gamerules.GameRule;

public class GameRuleChangedEvent {
	private final GameRule<?> gameRule;
	private final MinecraftServer server;

	public GameRuleChangedEvent(GameRule<?> gameRule, MinecraftServer server) {
		this.gameRule = gameRule;
		this.server = server;
	}

	public GameRule<?> getGameRule() {
		return gameRule;
	}

	public MinecraftServer getServer() {
		return server;
	}
}
