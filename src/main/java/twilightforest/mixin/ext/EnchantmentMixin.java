package twilightforest.mixin.ext;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import twilightforest.port.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.NeoHooks;

/**
 * Fabric port: IItemExtension#isPrimaryItemFor and #supportsEnchantment.
 */
@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
	private Holder<Enchantment> tf$holder() {
		RegistryAccess access = ServerLifecycleHooks.getCurrentRegistryAccess();
		Enchantment self = (Enchantment) (Object) this;
		return access == null ? Holder.direct(self) : access.lookupOrThrow(Registries.ENCHANTMENT).wrapAsHolder(self);
	}

	@ModifyReturnValue(method = "isPrimaryItem", at = @At("RETURN"))
	private boolean tf$isPrimaryItemFor(boolean original, ItemStack stack) {
		return NeoHooks.overrides(stack.getItem(), "isPrimaryItemFor", 2) ? stack.getItem().isPrimaryItemFor(stack, this.tf$holder()) : original;
	}

	@ModifyReturnValue(method = "isSupportedItem", at = @At("RETURN"))
	private boolean tf$supportsEnchantment(boolean original, ItemStack stack) {
		return NeoHooks.overrides(stack.getItem(), "supportsEnchantment", 2) ? stack.getItem().supportsEnchantment(stack, this.tf$holder()) : original;
	}
}
