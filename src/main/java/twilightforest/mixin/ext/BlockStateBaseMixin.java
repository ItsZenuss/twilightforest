package twilightforest.mixin.ext;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.fabric.NeoHooks;

/**
 * Fabric port: IBlockExtension#getCloneItemStack and #getLightEmission.
 */
@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {
	@Shadow
	public abstract Block getBlock();

	@Shadow
	protected abstract BlockState asState();

	@Inject(method = "getCloneItemStack", at = @At("HEAD"), cancellable = true)
	private void tf$cloneItemStack(LevelReader level, BlockPos pos, boolean includeData, CallbackInfoReturnable<ItemStack> cir) {
		if (NeoHooks.overrides(this.getBlock(), "getCloneItemStack", 5)) {
			cir.setReturnValue(this.getBlock().getCloneItemStack(level, pos, this.asState(), includeData, null));
		}
	}

	// TF's light overrides only depend on the state, so the position-less vanilla query can use them.
	@ModifyReturnValue(method = "getLightEmission", at = @At("RETURN"))
	private int tf$lightEmission(int original) {
		if (NeoHooks.overrides(this.getBlock(), "getLightEmission", 3)) {
			return this.getBlock().getLightEmission(this.asState(), null, null);
		}
		return original;
	}
}
