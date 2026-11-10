package twilightforest.mixin.ext;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.NeoHooks;

/**
 * Fabric port: IItemExtension#canWalkOnPowderedSnow.
 */
@Mixin(PowderSnowBlock.class)
public abstract class PowderSnowBlockMixin {
	@ModifyReturnValue(method = "canEntityWalkOnPowderSnow", at = @At("RETURN"))
	private static boolean tf$canWalkOnPowderedSnow(boolean original, Entity entity) {
		if (!original && entity instanceof LivingEntity living) {
			ItemStack boots = living.getItemBySlot(EquipmentSlot.FEET);
			return NeoHooks.overrides(boots.getItem(), "canWalkOnPowderedSnow", 2) && boots.getItem().canWalkOnPowderedSnow(boots, living);
		}
		return original;
	}
}
