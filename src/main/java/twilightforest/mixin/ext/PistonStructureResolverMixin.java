package twilightforest.mixin.ext;

import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.fabric.NeoHooks;

/**
 * Fabric port: IBlockExtension#isStickyBlock and #canStickTo.
 */
@Mixin(PistonStructureResolver.class)
public abstract class PistonStructureResolverMixin {
	@Inject(method = "isSticky", at = @At("HEAD"), cancellable = true)
	private static void tf$isSticky(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (NeoHooks.overrides(state.getBlock(), "isStickyBlock", 1)) {
			cir.setReturnValue(state.getBlock().isStickyBlock(state));
		}
	}

	@Inject(method = "canStickToEachOther", at = @At("HEAD"), cancellable = true)
	private static void tf$canStick(BlockState a, BlockState b, CallbackInfoReturnable<Boolean> cir) {
		boolean aCustom = NeoHooks.overrides(a.getBlock(), "canStickTo", 2);
		boolean bCustom = NeoHooks.overrides(b.getBlock(), "canStickTo", 2);
		if (aCustom || bCustom) {
			cir.setReturnValue((!aCustom || a.getBlock().canStickTo(a, b)) && (!bCustom || b.getBlock().canStickTo(b, a)));
		}
	}
}
