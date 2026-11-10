package twilightforest.port.neoforged.neoforge.common;

import net.minecraft.world.entity.player.Player;

public class CommonHooks {
	private static final ThreadLocal<Player> CRAFTING_PLAYER = new ThreadLocal<>();

	public static void setCraftingPlayer(Player player) {
		if (player == null) {
			CRAFTING_PLAYER.remove();
		} else {
			CRAFTING_PLAYER.set(player);
		}
	}

	public static Player getCraftingPlayer() {
		return CRAFTING_PLAYER.get();
	}
}
