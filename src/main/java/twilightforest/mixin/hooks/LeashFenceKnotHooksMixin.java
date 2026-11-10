package twilightforest.mixin.hooks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.BlockHooks;

/**
 * Fabric port of the NeoForge coremod transformer {@code twilightforest.asm.transformers.lead.LeashFenceKnotSurvivesTransformer}.
 */
@Mixin(LeashFenceKnotEntity.class)
public abstract class LeashFenceKnotHooksMixin {
	@ModifyReturnValue(method = "survives", at = @At("RETURN"))
	private boolean tf$survives(boolean original) {
		return BlockHooks.leashFenceKnotSurvives(original, (LeashFenceKnotEntity) (Object) this);
	}
}
