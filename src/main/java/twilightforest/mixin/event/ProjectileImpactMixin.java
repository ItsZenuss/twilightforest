package twilightforest.mixin.event;

import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.phys.HitResult;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Fabric port: fires NeoForge's ProjectileImpactEvent before a projectile processes a hit.
 */
@Mixin(Projectile.class)
public abstract class ProjectileImpactMixin {
	@Inject(method = "hitTargetOrDeflectSelf", at = @At("HEAD"), cancellable = true)
	private void tf$fireImpact(HitResult hit, CallbackInfoReturnable<ProjectileDeflection> cir) {
		if (hit.getType() == HitResult.Type.MISS) return;
		if (NeoForge.EVENT_BUS.post(new ProjectileImpactEvent((Projectile) (Object) this, hit)).isCanceled()) {
			cir.setReturnValue(ProjectileDeflection.NONE);
		}
	}
}
