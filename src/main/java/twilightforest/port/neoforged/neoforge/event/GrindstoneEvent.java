package twilightforest.port.neoforged.neoforge.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import twilightforest.port.neoforged.bus.api.Event;
import twilightforest.port.neoforged.bus.api.ICancellableEvent;

public abstract class GrindstoneEvent extends Event {
	public static class OnPlaceItem extends GrindstoneEvent implements ICancellableEvent {
		private final ItemStack topItem;
		private final ItemStack bottomItem;
		private ItemStack output = ItemStack.EMPTY;
		private int xp = -1;
		private boolean canceled;

		public OnPlaceItem(ItemStack topItem, ItemStack bottomItem) {
			this.topItem = topItem;
			this.bottomItem = bottomItem;
		}

		public ItemStack getTopItem() {
			return topItem;
		}

		public ItemStack getBottomItem() {
			return bottomItem;
		}

		public ItemStack getOutput() {
			return output;
		}

		public void setOutput(ItemStack output) {
			this.output = output;
		}

		public int getXp() {
			return xp;
		}

		public void setXp(int xp) {
			this.xp = xp;
		}

		@Override
		public boolean isCanceled() {
			return canceled;
		}

		@Override
		public void setCanceled(boolean cancel) {
			this.canceled = cancel;
		}
	}

	public static class OnTakeItem extends GrindstoneEvent {
		private final ItemStack topItem;
		private final ItemStack bottomItem;
		private final Player player;

		public OnTakeItem(ItemStack topItem, ItemStack bottomItem, Player player) {
			this.topItem = topItem;
			this.bottomItem = bottomItem;
			this.player = player;
		}

		public ItemStack getTopItem() {
			return topItem;
		}

		public ItemStack getBottomItem() {
			return bottomItem;
		}

		public Player getPlayer() {
			return player;
		}
	}
}
