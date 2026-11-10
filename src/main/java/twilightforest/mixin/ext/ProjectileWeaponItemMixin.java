package twilightforest.mixin.ext;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.NeoHooks;

/**
 * Fabric port: IItemExtension#customArrow.
 */
@Mixin(ProjectileWeaponItem.class)
public abstract class ProjectileWeaponItemMixin {
	@ModifyReturnValue(method = "createProjectile", at = @At("RETURN"))
	private Projectile tf$customArrow(Projectile original, Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean crit) {
		if (original instanceof AbstractArrow arrow && NeoHooks.overrides(this, "customArrow", 3)) {
			return ((ProjectileWeaponItem) (Object) this).customArrow(arrow, ammo, weapon);
		}
		return original;
	}
}
