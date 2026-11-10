package twilightforest.port.neoforged.neoforge.event;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import twilightforest.port.neoforged.bus.api.Event;
import twilightforest.port.neoforged.neoforge.common.tooltip.TooltipAppender;

import java.util.ArrayList;
import java.util.List;

/**
 * Fabric port: collects component tooltip appenders, applied by the client's ItemTooltipCallback
 * (lines are appended after vanilla's tooltip).
 */
public class RegisterTooltipAppendersEvent extends Event {
	private record Entry(DataComponentType<?> type, TooltipAppender appender) {}

	private static final List<Entry> APPENDERS = new ArrayList<>();

	public <T> void registerComponentAppenderAfter(DataComponentType<T> type, DataComponentType<?> after, TooltipAppender appender) {
		APPENDERS.add(new Entry(type, appender));
	}

	public static void appendAll(ItemStack stack, Item.TooltipContext context, TooltipFlag flag, List<Component> lines) {
		for (Entry entry : APPENDERS) {
			if (stack.has(entry.type())) entry.appender().append(stack, context, flag, lines::add);
		}
	}
}
