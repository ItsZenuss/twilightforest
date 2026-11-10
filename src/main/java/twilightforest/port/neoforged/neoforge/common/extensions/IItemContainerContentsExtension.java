package twilightforest.port.neoforged.neoforge.common.extensions;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.List;

public interface IItemContainerContentsExtension {
	default int getSlots() {
		if (this instanceof ItemContainerContents contents) {
			return (int) contents.allItemsCopyStream().count();
		}
		return 0;
	}

	default ItemStack getStackInSlot(int slot) {
		if (this instanceof ItemContainerContents contents) {
			List<ItemStack> list = contents.allItemsCopyStream().toList();
			if (slot >= 0 && slot < list.size()) {
				return list.get(slot);
			}
		}
		return ItemStack.EMPTY;
	}
}
