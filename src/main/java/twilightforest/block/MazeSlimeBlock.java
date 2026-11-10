package twilightforest.block;

import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFBlocks;
import twilightforest.tags.TFBlockTags;

public class MazeSlimeBlock extends SlimeBlock {
	public MazeSlimeBlock(Properties properties) {
		super(properties);
	}

	public boolean canStickTo(BlockState state, BlockState other) {
		return other.is(TFBlockTags.STORAGE_BLOCKS_MAZE_SLIME) || other.is(TFBlocks.MAZESTONE);
	}

	public boolean isStickyBlock(BlockState state) {
		return true;
	}
}
