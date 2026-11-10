package twilightforest.port.neoforged.neoforge.event.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.port.neoforged.bus.api.Event;
import twilightforest.port.neoforged.bus.api.ICancellableEvent;
import twilightforest.port.neoforged.neoforge.common.util.BlockSnapshot;

import java.util.Collections;
import java.util.List;

public class BlockEvent extends Event {
	private final LevelAccessor level;
	private final BlockPos pos;
	private final BlockState state;

	public BlockEvent(LevelAccessor level, BlockPos pos, BlockState state) {
		this.level = level;
		this.pos = pos;
		this.state = state;
	}

	public LevelAccessor getLevel() {
		return level;
	}

	public BlockPos getPos() {
		return pos;
	}

	public BlockState getState() {
		return state;
	}

	public static class EntityMultiPlaceEvent extends BlockEvent implements ICancellableEvent {
		private final Entity entity;
		private final List<BlockSnapshot> replacedBlockSnapshots;
		private boolean canceled;

		public EntityMultiPlaceEvent(List<BlockSnapshot> replacedBlockSnapshots, BlockState placedAgainst, Entity entity) {
			super(replacedBlockSnapshots.isEmpty() ? null : (LevelAccessor) entity.level(), replacedBlockSnapshots.isEmpty() ? BlockPos.ZERO : replacedBlockSnapshots.get(0).getPos(), placedAgainst);
			this.replacedBlockSnapshots = replacedBlockSnapshots;
			this.entity = entity;
		}

		public Entity getEntity() {
			return entity;
		}

		public List<BlockSnapshot> getReplacedBlockSnapshots() {
			return Collections.unmodifiableList(replacedBlockSnapshots);
		}

		@Override
		public boolean isCanceled() {
			return canceled;
		}

		@Override
		public void setCanceled(boolean canceled) {
			this.canceled = canceled;
		}
	}
}
