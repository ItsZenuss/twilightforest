package twilightforest.port.neoforged.neoforge.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class BlockSnapshot {
	private final BlockPos pos;
	private final BlockState state;

	public BlockSnapshot(BlockPos pos, BlockState state) {
		this.pos = pos;
		this.state = state;
	}

	public BlockPos getPos() {
		return pos;
	}

	public BlockState getCurrentState() {
		return state;
	}
}
