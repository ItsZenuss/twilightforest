package twilightforest.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.EntityHooks;

/**
 * Fabric port of the NeoForge coremod transformer {@code twilightforest.asm.transformers.entity.WaterSprintTransformer}.
 */
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerHooksMixin {
	@ModifyExpressionValue(method = "shouldStopSwimSprinting", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isInWater()Z"))
	private boolean tf$sprintInWater(boolean original) {
		return EntityHooks.unrestrainedSprintingInWater(original, (LocalPlayer) (Object) this);
	}

	@ModifyExpressionValue(method = "isSprintingPossible", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isInShallowWater()Z"))
	private boolean tf$sprintInShallowWater(boolean original) {
		return EntityHooks.unrestrainedSprintingInWater(original, (LocalPlayer) (Object) this);
	}
}
