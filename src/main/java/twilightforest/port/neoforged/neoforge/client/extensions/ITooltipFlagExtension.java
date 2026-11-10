package twilightforest.port.neoforged.neoforge.client.extensions;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;

public interface ITooltipFlagExtension {
	default boolean hasShiftDown() {
		try {
			Minecraft mc = Minecraft.getInstance();
			if (mc != null && mc.getWindow() != null) {
				return InputConstants.isKeyDown(mc.getWindow(), InputConstants.KEY_LSHIFT) ||
						InputConstants.isKeyDown(mc.getWindow(), InputConstants.KEY_RSHIFT);
			}
		} catch (Throwable ignored) {}
		return false;
	}
}
