package twilightforest.port.neoforged.neoforge.client.event;

import net.fabricmc.fabric.api.client.rendering.v1.ClientTooltipComponentCallback;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import twilightforest.port.neoforged.bus.api.Event;

import java.util.function.Function;

public class RegisterClientTooltipComponentFactoriesEvent extends Event {
	public <T extends TooltipComponent> void register(Class<T> type, Function<T, ClientTooltipComponent> factory) {
		ClientTooltipComponentCallback.EVENT.register(component -> type.isInstance(component) ? factory.apply(type.cast(component)) : null);
	}
}
