package twilightforest.port.neoforged.neoforge.event.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import twilightforest.port.neoforged.bus.api.ICancellableEvent;

public abstract class PlayerInteractEvent extends PlayerEvent implements ICancellableEvent {
	private final InteractionHand hand;
	private final BlockPos pos;
	@org.jetbrains.annotations.Nullable
	private final Direction face;
	private boolean canceled;
	private InteractionResult cancellationResult = InteractionResult.PASS;

	public PlayerInteractEvent(Player player, InteractionHand hand, BlockPos pos, Direction face) {
		super(player);
		this.hand = hand;
		this.pos = pos;
		this.face = face;
	}

	public InteractionHand getHand() {
		return hand;
	}

	public BlockPos getPos() {
		return pos;
	}

	public Direction getFace() {
		return face;
	}

	public Level getLevel() {
		return getEntity().level();
	}

	public ItemStack getItemStack() {
		return getEntity().getItemInHand(hand);
	}

	public InteractionResult getCancellationResult() {
		return cancellationResult;
	}

	public void setCancellationResult(InteractionResult result) {
		this.cancellationResult = result;
	}

	@Override
	public boolean isCanceled() {
		return canceled;
	}

	@Override
	public void setCanceled(boolean cancel) {
		this.canceled = cancel;
	}

	public static class RightClickBlock extends PlayerInteractEvent {
		private final BlockHitResult hitVec;

		public RightClickBlock(Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitVec) {
			super(player, hand, pos, hitVec != null ? hitVec.getDirection() : null);
			this.hitVec = hitVec;
		}

		public BlockHitResult getHitVec() {
			return hitVec;
		}

		private net.minecraft.util.TriState useBlock = net.minecraft.util.TriState.DEFAULT;
		private net.minecraft.util.TriState useItem = net.minecraft.util.TriState.DEFAULT;

		public void setUseBlock(net.minecraft.util.TriState useBlock) {
			this.useBlock = useBlock;
		}

		public net.minecraft.util.TriState getUseBlock() {
			return useBlock;
		}

		public void setUseItem(net.minecraft.util.TriState useItem) {
			this.useItem = useItem;
		}

		public net.minecraft.util.TriState getUseItem() {
			return useItem;
		}
	}

	public static class LeftClickEmpty extends PlayerInteractEvent {
		public LeftClickEmpty(Player player) {
			super(player, InteractionHand.MAIN_HAND, player.blockPosition(), null);
		}
	}

	public static class RightClickItem extends PlayerInteractEvent {
		public RightClickItem(Player player, InteractionHand hand) {
			super(player, hand, player.blockPosition(), null);
		}
	}
}
