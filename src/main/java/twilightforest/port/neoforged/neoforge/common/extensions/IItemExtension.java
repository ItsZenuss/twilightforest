package twilightforest.port.neoforged.neoforge.common.extensions;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

public interface IItemExtension {
	default int getMaxStackSize(ItemStack stack) {
		if (this instanceof Item item) {
			return item.getDefaultMaxStackSize();
		}
		return 64;
	}

	default boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) {
		return enchantment.value().isPrimaryItem(stack);
	}

	default boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
		return enchantment.value().isSupportedItem(stack);
	}

	default boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
		return false;
	}

	default boolean isGazeDisguise(ItemStack stack, Player player, @Nullable LivingEntity entity) {
		return false;
	}

	default boolean canFitInsideContainerItems(ItemStack stack) {
		if (this instanceof Item item) {
			return item.canFitInsideContainerItems();
		}
		return true;
	}

	default boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
		return ItemStack.isSameItem(oldStack, newStack);
	}

	default boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return !oldStack.equals(newStack);
	}

	default net.minecraft.world.entity.projectile.arrow.AbstractArrow customArrow(net.minecraft.world.entity.projectile.arrow.AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
		return arrow;
	}

	default <T extends LivingEntity> int damageItem(ItemStack stack, int amount, @Nullable T entity, java.util.function.Consumer<Item> onBroken) {
		return amount;
	}

	default boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
		return false;
	}
}
