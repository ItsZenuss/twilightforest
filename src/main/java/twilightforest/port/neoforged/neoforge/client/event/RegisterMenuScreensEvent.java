package twilightforest.port.neoforged.neoforge.client.event;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import twilightforest.port.neoforged.bus.api.Event;

public class RegisterMenuScreensEvent extends Event {
	public <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void register(MenuType<? extends M> type, MenuScreens.ScreenConstructor<M, U> constructor) {
		MenuScreens.register(type, constructor);
	}
}
