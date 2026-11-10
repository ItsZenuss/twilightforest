package twilightforest.port.neoforged.neoforge.common.tooltip;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.function.Consumer;

/**
 * Fabric port of NeoForge's TooltipAppender: adds tooltip lines for a data component.
 */
@FunctionalInterface
public interface TooltipAppender {
	void append(ItemStack stack, Item.TooltipContext context, TooltipFlag flag, Consumer<Component> tooltip);

	/**
	 * Appends the component's own lines when its value is a vanilla {@link TooltipProvider}.
	 */
	static <T> TooltipAppender createComponentAppender(DataComponentType<T> type) {
		return (stack, context, flag, tooltip) -> {
			T value = stack.get(type);
			if (value instanceof TooltipProvider provider) {
				provider.addToTooltip(context, tooltip, flag, stack);
			}
		};
	}
}
