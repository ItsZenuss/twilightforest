package twilightforest.mixin.hooks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.level.block.SnowyBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.BlockHooks;

/**
 * Fabric port of the NeoForge coremod transformer {@code twilightforest.asm.transformers.snow.KeepGrassSnowyForSnowloggableBlocksTransformer}.
 */
@Mixin(SnowyBlock.class)
public abstract class SnowyBlockHooksMixin {
	@ModifyReturnValue(method = "isSnowySetting", at = @At("RETURN"))
	private static boolean tf$keepSnowy(boolean original, BlockState state) {
		return BlockHooks.keepSnowyStateForSnowloggableBlocks(original, state);
	}
}
