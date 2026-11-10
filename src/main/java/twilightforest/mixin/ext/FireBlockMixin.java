package twilightforest.mixin.ext;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.NeoHooks;

/**
 * Fabric port: IBlockExtension#onCaughtFire and #isFireSource.
 */
@Mixin(FireBlock.class)
public abstract class FireBlockMixin {
	// NeoForge replaced the TNT special case with IBlockExtension#onCaughtFire.
	@ModifyExpressionValue(method = "checkBurnOut", at = @At(value = "CONSTANT", args = "classValue=net/minecraft/world/level/block/TntBlock"))
	private boolean tf$onCaughtFire(boolean isTnt, Level level, BlockPos pos, int chance, RandomSource random, int age, @Local BlockState state) {
		if (!isTnt && NeoHooks.overrides(state.getBlock(), "onCaughtFire", 5)) {
			state.getBlock().onCaughtFire(state, level, pos, null, null);
		}
		return isTnt;
	}

	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 0))
	private boolean tf$isFireSource(BlockState below, TagKey<Block> infiniburn, Operation<Boolean> original, BlockState fire, ServerLevel level, BlockPos pos, RandomSource random) {
		if (NeoHooks.overrides(below.getBlock(), "isFireSource", 4) && below.getBlock().isFireSource(below, level, pos.below(), Direction.UP)) {
			return true;
		}
		return original.call(below, infiniburn);
	}
}
