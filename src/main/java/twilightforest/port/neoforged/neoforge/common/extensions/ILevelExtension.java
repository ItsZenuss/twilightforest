package twilightforest.port.neoforged.neoforge.common.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;

public interface ILevelExtension {
	default boolean isAreaLoaded(BlockPos center, int range) {
		if (this instanceof LevelReader reader) {
			return reader.hasChunksAt(center.offset(-range, -range, -range), center.offset(range, range, range));
		}
		return false;
	}
}
