package twilightforest.port.neoforged.neoforge.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.item.crafting.RecipeType;
import twilightforest.port.neoforged.bus.api.Event;
import org.jetbrains.annotations.Nullable;

public class OnDatapackSyncEvent extends Event {
	@Nullable
	private final ServerPlayer player;
	@Nullable
	private final PlayerList playerList;

	public OnDatapackSyncEvent() {
		this(null, null);
	}

	public OnDatapackSyncEvent(@Nullable ServerPlayer player, @Nullable PlayerList playerList) {
		this.player = player;
		this.playerList = playerList;
	}

	@Nullable
	public ServerPlayer getPlayer() {
		return player;
	}

	@Nullable
	public PlayerList getPlayerList() {
		return playerList;
	}

	public void sendRecipes(RecipeType<?>... types) {}
}
