package twilightforest.mixin.hooks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.util.TriState;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.BlockHooks;

/**
 * Fabric port of the NeoForge coremod transformer {@code twilightforest.asm.transformers.shroom.ModifySoilDecisionForMushroomBlockSurvivabilityTransformer}.
 */
@Mixin(MushroomBlock.class)
public abstract class MushroomBlockHooksMixin {
	// Vanilla has no soil-sustain TriState here; ask the hook only when vanilla says the mushroom can't survive.
	@ModifyReturnValue(method = "canSurvive", at = @At("RETURN"))
	private boolean tf$portalSoil(boolean original, BlockState state, LevelReader level, BlockPos pos) {
		return original || BlockHooks.modifySoilDecisionForMushroomBlockSurvivability(TriState.DEFAULT, level, pos) == TriState.TRUE;
	}
}
