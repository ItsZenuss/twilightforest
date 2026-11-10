package twilightforest.mixin.hooks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.PathfinderMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.EntityHooks;

/**
 * Fabric port of the NeoForge coremod transformer {@code twilightforest.asm.transformers.entity.PathFinderUnrestrainedByLeashTransformer}.
 */
@Mixin(PathfinderMob.class)
public abstract class PathfinderMobHooksMixin {
	@ModifyReturnValue(method = "shouldStayCloseToLeashHolder", at = @At("RETURN"))
	private boolean tf$leash(boolean original) {
		return EntityHooks.overrideStayCloseToHolder(original, (PathfinderMob) (Object) this);
	}
}
