package twilightforest.port.neoforged.neoforge.event.entity.player;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import twilightforest.port.neoforged.bus.api.Event;

import java.util.List;

public class ItemTooltipEvent extends Event {
	private final ItemStack itemStack;
	private final List<Component> toolTip;

	public ItemTooltipEvent(ItemStack itemStack, List<Component> toolTip) {
		this.itemStack = itemStack;
		this.toolTip = toolTip;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public List<Component> getToolTip() {
		return toolTip;
	}
}
