package twilightforest.port.neoforged.neoforge.event.entity.player;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.world.entity.player.Player;

public class AdvancementEvent extends PlayerEvent {
	private final AdvancementHolder advancement;

	public AdvancementEvent(Player player, AdvancementHolder advancement) {
		super(player);
		this.advancement = advancement;
	}

	public AdvancementHolder getAdvancement() {
		return advancement;
	}

	public static class AdvancementEarnEvent extends AdvancementEvent {
		public AdvancementEarnEvent(Player player, AdvancementHolder advancement) {
			super(player, advancement);
		}
	}
}
