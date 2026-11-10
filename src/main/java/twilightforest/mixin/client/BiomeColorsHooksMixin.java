package twilightforest.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.BlockHooks;

/**
 * Fabric port of the NeoForge coremod transformer {@code twilightforest.asm.transformers.foliage.FoliageColorResolverTransformer}.
 */
@Mixin(BiomeColors.class)
public abstract class BiomeColorsHooksMixin {
	@ModifyExpressionValue(method = {"lambda$static$0", "lambda$static$1", "lambda$static$2"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getFoliageColor()I"))
	private static int tf$foliage(int original, @Local(argsOnly = true) Biome biome, @Local(argsOnly = true, ordinal = 0) double x, @Local(argsOnly = true, ordinal = 1) double z) {
		return BlockHooks.resolveFoliageColor(original, biome, x, z);
	}
}
