package twilightforest.mixin.ext;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.NeoHooks;

/**
 * Fabric port: IBlockExtension#canEntityDestroy for the ender dragon.
 */
@Mixin(EnderDragon.class)
public abstract class BlockDestroyedByEntitiesMixin {
	// Blocks that refuse the dragon are treated as dragon-immune.
	@WrapOperation(method = "checkWalls", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 1))
	private boolean tf$canEntityDestroy(BlockState state, TagKey<Block> tag, Operation<Boolean> original, @Local(argsOnly = true) ServerLevel level, @Local BlockPos pos) {
		if (NeoHooks.overrides(state.getBlock(), "canEntityDestroy", 4) && !state.getBlock().canEntityDestroy(state, level, pos, (EnderDragon) (Object) this)) {
			return true;
		}
		return original.call(state, tag);
	}
}
