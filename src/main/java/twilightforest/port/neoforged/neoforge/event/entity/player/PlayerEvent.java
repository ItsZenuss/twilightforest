package twilightforest.port.neoforged.neoforge.event.entity.player;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import twilightforest.port.neoforged.neoforge.event.entity.EntityEvent;

public class PlayerEvent extends EntityEvent {
	private final Player player;

	public PlayerEvent(Player player) {
		super(player);
		this.player = player;
	}

	@Override
	public Player getEntity() {
		return player;
	}

	public static class PlayerLoggedInEvent extends PlayerEvent {
		public PlayerLoggedInEvent(Player player) {
			super(player);
		}
	}

	public static class PlayerLoggedOutEvent extends PlayerEvent {
		public PlayerLoggedOutEvent(Player player) {
			super(player);
		}
	}

	public static class PlayerRespawnEvent extends PlayerEvent {
		private final boolean endConquered;

		public PlayerRespawnEvent(Player player, boolean endConquered) {
			super(player);
			this.endConquered = endConquered;
		}

		public boolean isEndConquered() {
			return endConquered;
		}
	}

	public static class ItemCraftedEvent extends PlayerEvent {
		private final ItemStack crafting;
		private final net.minecraft.world.Container inventory;

		public ItemCraftedEvent(Player player, ItemStack crafting) {
			this(player, crafting, new net.minecraft.world.SimpleContainer(0));
		}

		public ItemCraftedEvent(Player player, ItemStack crafting, net.minecraft.world.Container inventory) {
			super(player);
			this.crafting = crafting;
			this.inventory = inventory;
		}

		public net.minecraft.world.Container getInventory() {
			return inventory;
		}

		public ItemStack getCrafting() {
			return crafting;
		}
	}

	public static class Clone extends PlayerEvent {
		private final Player original;
		private final boolean wasDeath;

		public Clone(Player _new, Player original, boolean wasDeath) {
			super(_new);
			this.original = original;
			this.wasDeath = wasDeath;
		}

		public Player getOriginal() {
			return original;
		}

		public boolean isWasDeath() {
			return wasDeath;
		}
	}
}
