package twilightforest.mixin.client;

import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.world.item.ItemStack;
import twilightforest.port.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import twilightforest.port.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Fabric port: lets items with a NeoForge-style client extension swap in their own armor model
 * (IClientItemExtensions#getHumanoidArmorModel), as NeoForge does.
 */
@Mixin(EquipmentLayerRenderer.class)
public abstract class EquipmentLayerRendererMixin {
	@ModifyVariable(
		method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V",
		at = @At("HEAD"), argsOnly = true)
	@SuppressWarnings({"rawtypes", "unchecked"})
	private Model tf$swapArmorModel(Model original, EquipmentClientInfo.LayerType layerType, net.minecraft.resources.ResourceKey<?> asset, Model model, Object state, ItemStack stack,
									com.mojang.blaze3d.vertex.PoseStack poseStack, net.minecraft.client.renderer.SubmitNodeCollector collector, int light, net.minecraft.resources.Identifier texture, int outlineColor, int order) {
		IClientItemExtensions ext = RegisterClientExtensionsEvent.getItemExtensions(stack.getItem());
		if (ext == null) return original;
		Model replacement = ext.getHumanoidArmorModel(stack, layerType, original);
		return replacement != null ? replacement : original;
	}

	private static final String RENDER_LAYERS = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V";

	// IClientItemExtensions#getDefaultDyeColor: the colour used when a dyeable armor piece has not been dyed.
	@org.spongepowered.asm.mixin.injection.ModifyArg(method = RENDER_LAYERS, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/DyedItemColor;getOrDefault(Lnet/minecraft/world/item/ItemStack;I)I"), index = 1)
	private int tf$defaultDyeColor(int original, @com.llamalad7.mixinextras.sugar.Local(argsOnly = true) ItemStack stack) {
		IClientItemExtensions ext = RegisterClientExtensionsEvent.getItemExtensions(stack.getItem());
		if (ext != null) {
			int color = ext.getDefaultDyeColor(stack);
			if (color != 0) return color;
		}
		return original;
	}

	// IClientItemExtensions#getArmorTexture
	@com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation(method = RENDER_LAYERS, at = @At(value = "INVOKE", target = "Ljava/util/function/Function;apply(Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0))
	private Object tf$armorTexture(java.util.function.Function<Object, Object> lookup, Object key, com.llamalad7.mixinextras.injector.wrapoperation.Operation<Object> original,
								   @com.llamalad7.mixinextras.sugar.Local(argsOnly = true) EquipmentClientInfo.LayerType layerType, @com.llamalad7.mixinextras.sugar.Local(argsOnly = true) ItemStack stack,
								   @com.llamalad7.mixinextras.sugar.Local EquipmentClientInfo.Layer layer) {
		Object texture = original.call(lookup, key);
		IClientItemExtensions ext = RegisterClientExtensionsEvent.getItemExtensions(stack.getItem());
		if (ext != null && texture instanceof net.minecraft.resources.Identifier id) {
			return ext.getArmorTexture(stack, layerType, layer, id);
		}
		return texture;
	}
}
