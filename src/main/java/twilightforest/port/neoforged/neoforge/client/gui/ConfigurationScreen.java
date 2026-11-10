package twilightforest.port.neoforged.neoforge.client.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigurationScreen extends Screen {
	private final Screen parent;

	public ConfigurationScreen(Screen parent) {
		super(Component.literal("Twilight Forest Configuration"));
		this.parent = parent;
	}

	@Override
	public void onClose() {
		if (this.minecraft != null) {
			this.minecraft.setScreen(this.parent);
		}
	}
}
