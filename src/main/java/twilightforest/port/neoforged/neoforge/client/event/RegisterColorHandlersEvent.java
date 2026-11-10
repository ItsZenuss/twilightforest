package twilightforest.port.neoforged.neoforge.client.event;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import twilightforest.port.neoforged.bus.api.Event;

import java.util.List;

public class RegisterColorHandlersEvent extends Event {
	public static class BlockTintSources extends RegisterColorHandlersEvent {
		private final BlockColors blockColors;

		public BlockTintSources(BlockColors blockColors) {
			this.blockColors = blockColors;
		}

		public BlockColors getBlockColors() {
			return blockColors;
		}

		public void register(List<BlockTintSource> sources, Block... blocks) {
			BlockColorRegistry.register(sources, blocks);
		}
	}

	public static class ItemTintSources extends RegisterColorHandlersEvent {
		public void register(Identifier id, MapCodec<? extends ItemTintSource> type) {
			net.minecraft.client.color.item.ItemTintSources.ID_MAPPER.put(id, type);
		}
	}
}
