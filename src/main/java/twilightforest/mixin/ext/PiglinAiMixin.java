package twilightforest.mixin.ext;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.NeoHooks;

/**
 * Fabric port: IItemExtension#makesPiglinsNeutral.
 */
@Mixin(PiglinAi.class)
public abstract class PiglinAiMixin {
	@ModifyReturnValue(method = "isWearingSafeArmor", at = @At("RETURN"))
	private static boolean tf$makesPiglinsNeutral(boolean original, LivingEntity entity) {
		if (original) return true;
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (!slot.isArmor()) continue;
			ItemStack stack = entity.getItemBySlot(slot);
			if (NeoHooks.overrides(stack.getItem(), "makesPiglinsNeutral", 2) && stack.getItem().makesPiglinsNeutral(stack, entity)) return true;
		}
		return false;
	}
}
