package twilightforest.mixin.hooks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.MapHooks;

/**
 * Fabric port of the NeoForge coremod transformer {@code twilightforest.asm.transformers.map.ResolveNearestNonRandomSpreadMapStructureTransformer}.
 */
@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorHooksMixin {
	@ModifyReturnValue(method = "findNearestMapStructure", at = @At("RETURN"))
	private Pair<BlockPos, Holder<Structure>> tf$nonRandomSpread(Pair<BlockPos, Holder<Structure>> original, ServerLevel level, HolderSet<Structure> structures, BlockPos pos, int radius, boolean skipKnown) {
		return MapHooks.resolveNearestNonRandomSpreadMapStructure(original, level, structures, pos, radius, skipKnown);
	}
}
