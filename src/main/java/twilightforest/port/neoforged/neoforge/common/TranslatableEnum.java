package twilightforest.port.neoforged.neoforge.common;

import net.minecraft.network.chat.Component;

public interface TranslatableEnum {
	default Component getTranslatedName() {
		return Component.empty();
	}
}
