package twilightforest.mixin.hooks;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import twilightforest.asmhooks.WorldgenHooks;

/**
 * Fabric port of the NeoForge coremod transformer {@code twilightforest.asm.transformers.beardifier.InjectCustomTerrainBeardifierDuringCreateNoiseChunkTransformer}.
 */
@Mixin(NoiseBasedChunkGenerator.class)
public abstract class NoiseBasedChunkGeneratorHooksMixin {
	@ModifyArg(method = "createNoiseChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/NoiseChunk;forChunk(Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/levelgen/DensityFunctions$BeardifierOrMarker;Lnet/minecraft/world/level/levelgen/NoiseGeneratorSettings;Lnet/minecraft/world/level/levelgen/Aquifer$FluidPicker;Lnet/minecraft/world/level/levelgen/blending/Blender;)Lnet/minecraft/world/level/levelgen/NoiseChunk;"), index = 2)
	private DensityFunctions.BeardifierOrMarker tf$customTerrain(DensityFunctions.BeardifierOrMarker vanilla, @Local(argsOnly = true) ChunkAccess chunk, @Local(argsOnly = true) StructureManager structureManager) {
		return vanilla instanceof Beardifier beardifier ? WorldgenHooks.gatherCustomTerrain(beardifier, structureManager, chunk) : vanilla;
	}
}
