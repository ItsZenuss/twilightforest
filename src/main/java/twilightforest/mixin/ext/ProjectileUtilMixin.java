package twilightforest.mixin.ext;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.NeoHooks;

/**
 * Fabric port: IEntityExtension#canRiderInteract. A vehicle that allows rider interaction (e.g. a yeti carrying the
 * player) is targetable like any other entity instead of only when nothing else is in the way.
 */
@Mixin(ProjectileUtil.class)
public abstract class ProjectileUtilMixin {
	@WrapOperation(method = "getEntityHitResult(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)Lnet/minecraft/world/phys/EntityHitResult;",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getRootVehicle()Lnet/minecraft/world/entity/Entity;", ordinal = 0))
	private static Entity tf$canRiderInteract(Entity candidate, Operation<Entity> original) {
		if (NeoHooks.overrides(candidate, "canRiderInteract", 0) && candidate.canRiderInteract()) {
			return null;
		}
		return original.call(candidate);
	}
}
