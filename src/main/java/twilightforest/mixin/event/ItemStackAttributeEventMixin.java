package twilightforest.mixin.event;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.ItemAttributeModifierEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Fabric port: fires ItemAttributeModifierEvent whenever a stack's attribute modifiers are applied or shown. */
@Mixin(ItemStack.class)
public abstract class ItemStackAttributeEventMixin {
	@ModifyExpressionValue(method = "forEachModifier*", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getOrDefault(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;"))
	private Object tf$attributeModifiers(Object value) {
		if (!(value instanceof ItemAttributeModifiers modifiers)) return value;
		ItemAttributeModifierEvent event = new ItemAttributeModifierEvent((ItemStack) (Object) this, modifiers);
		if (!NeoForge.EVENT_BUS.hasListeners(ItemAttributeModifierEvent.class)) return value;
		return NeoForge.EVENT_BUS.post(event).build();
	}
}
