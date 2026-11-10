package twilightforest.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.NeoHooks;

/**
 * Fabric port: IItemExtension#shouldCauseReequipAnimation (items whose data changes while held don't bob).
 */
@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
	@ModifyReturnValue(method = "shouldInstantlyReplaceVisibleItem", at = @At("RETURN"))
	private boolean tf$reequip(boolean original, ItemStack oldStack, ItemStack newStack) {
		if (!original && !newStack.isEmpty() && oldStack.getItem() == newStack.getItem()
			&& NeoHooks.overrides(newStack.getItem(), "shouldCauseReequipAnimation", 3)) {
			return !newStack.getItem().shouldCauseReequipAnimation(oldStack, newStack, false);
		}
		return original;
	}
}
