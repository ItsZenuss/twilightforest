package twilightforest.port.neoforged.neoforge.event;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Fabric port of NeoForge's BlockEntityTypeAddBlocksEvent: adds blocks to a block entity type's valid block set
 * (field opened by the access widener).
 */
public class BlockEntityTypeAddBlocksEvent {
	public void modify(BlockEntityType<?> type, Block... blocks) {
		Set<Block> valid = new HashSet<>(type.validBlocks);
		Collections.addAll(valid, blocks);
		type.validBlocks = Set.copyOf(valid);
	}
}
