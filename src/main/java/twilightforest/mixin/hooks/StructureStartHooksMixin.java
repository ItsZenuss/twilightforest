package twilightforest.mixin.hooks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.WorldgenHooks;

/**
 * Fabric port of the NeoForge coremod transformer {@code twilightforest.asm.transformers.conquered.StructureStartLoadStaticTransformer}.
 */
@Mixin(StructureStart.class)
public abstract class StructureStartHooksMixin {
	@ModifyExpressionValue(method = "loadStaticStart", at = @At(value = "NEW", target = "Lnet/minecraft/world/level/levelgen/structure/StructureStart;"))
	private static StructureStart tf$loadStatic(StructureStart start, @Local PiecesContainer pieces, @Local(argsOnly = true) CompoundTag nbt) {
		return WorldgenHooks.loadStaticStart(start, pieces, nbt);
	}
}
