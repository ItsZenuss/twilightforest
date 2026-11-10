package twilightforest.port.neoforged.neoforge.client.extensions.common;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public interface IClientItemExtensions {
	static IClientItemExtensions of(ItemLike item) {
		return new IClientItemExtensions() {};
	}

	@SuppressWarnings("rawtypes")
	default net.minecraft.client.model.Model<?> getHumanoidArmorModel(ItemStack stack, EquipmentClientInfo.LayerType layerType, net.minecraft.client.model.Model original) {
		return original;
	}

	default net.minecraft.resources.Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer, net.minecraft.resources.Identifier def) {
		return def;
	}

	default int getDefaultDyeColor(ItemStack stack) {
		return 0;
	}
}
