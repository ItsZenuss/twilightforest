package twilightforest.mixin.ext;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.NeoHooks;

/**
 * Fabric port: IBlockExtension#getBeaconColorMultiplier for beam-tinting blocks.
 */
@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin {
	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/DyeColor;getTextureDiffuseColor()I"))
	private static int tf$beaconColor(DyeColor color, Operation<Integer> original, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockPos beaconPos,
									  @Local(ordinal = 1) BlockState state, @Local(ordinal = 1) BlockPos pos) {
		if (NeoHooks.overrides(state.getBlock(), "getBeaconColorMultiplier", 4)) {
			Integer custom = state.getBlock().getBeaconColorMultiplier(state, level, pos, beaconPos);
			if (custom != null) return 0xFF000000 | custom;
		}
		return original.call(color);
	}
}
