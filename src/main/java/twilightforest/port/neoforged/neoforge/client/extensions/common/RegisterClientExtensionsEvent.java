package twilightforest.port.neoforged.neoforge.client.extensions.common;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import twilightforest.port.neoforged.bus.api.Event;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Fabric port: collects client item/block extensions. Item extensions drive custom armor models through
 * {@code twilightforest.mixin.client.EquipmentLayerRendererMixin}; block extensions (custom hit/destroy particles)
 * are stored but not yet hooked up.
 */
public class RegisterClientExtensionsEvent extends Event {
	private static final Map<Item, IClientItemExtensions> ITEMS = new IdentityHashMap<>();
	private static final Map<Block, IClientBlockExtensions> BLOCKS = new IdentityHashMap<>();

	public void registerBlock(IClientBlockExtensions ext, Block... blocks) {
		for (Block block : blocks) BLOCKS.put(block, ext);
	}

	public void registerItem(Object ext, ItemLike... items) {
		if (ext instanceof IClientItemExtensions itemExt) {
			for (ItemLike item : items) ITEMS.put(item.asItem(), itemExt);
		}
	}

	@Nullable
	public static IClientItemExtensions getItemExtensions(Item item) {
		return ITEMS.get(item);
	}

	@Nullable
	public static IClientBlockExtensions getBlockExtensions(Block block) {
		return BLOCKS.get(block);
	}
}
