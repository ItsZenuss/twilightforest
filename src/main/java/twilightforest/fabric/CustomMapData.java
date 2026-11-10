package twilightforest.fabric;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import twilightforest.item.MagicMapItem;
import twilightforest.item.MazeMapItem;

/**
 * Fabric port of NeoForge's MapItem#getCustomMapData: Twilight Forest maps keep their data outside the vanilla map
 * store, so map rendering asks the item for it.
 */
public final class CustomMapData {
	private CustomMapData() {}

	@Nullable
	public static MapItemSavedData get(ItemStack stack, Level level) {
		if (stack.getItem() instanceof MagicMapItem magic) return magic.getCustomMapData(stack, level);
		if (stack.getItem() instanceof MazeMapItem maze) return maze.getCustomMapData(stack, level);
		return null;
	}
}
