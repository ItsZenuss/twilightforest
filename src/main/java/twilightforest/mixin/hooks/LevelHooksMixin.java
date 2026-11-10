package twilightforest.mixin.hooks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.BlockHooks;

/**
 * Fabric port of the NeoForge coremod transformer {@code twilightforest.asm.transformers.cloud.IsRainingAtTransformer}.
 */
@Mixin(Level.class)
public abstract class LevelHooksMixin {
	@ModifyReturnValue(method = "isRainingAt", at = @At("RETURN"))
	private boolean tf$isRainingAt(boolean original, BlockPos pos) {
		return BlockHooks.isRainingAt(original, (Level) (Object) this, pos);
	}
}
