package twilightforest.mixin.ext;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.port.neoforged.neoforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.fabric.NeoHooks;

/**
 * Fabric port: IBlockExtension#addRunningEffects and IEntityExtension#isPushedByFluid(FluidType).
 */
@Mixin(Entity.class)
public abstract class EntityExtHooksMixin {
	@Inject(method = "spawnSprintParticle", at = @At("HEAD"), cancellable = true)
	private void tf$runningEffects(CallbackInfo ci) {
		Entity self = (Entity) (Object) this;
		BlockPos pos = self.getOnPosLegacy();
		BlockState state = self.level().getBlockState(pos);
		if (NeoHooks.overrides(state.getBlock(), "addRunningEffects", 4) && state.getBlock().addRunningEffects(state, self.level(), pos, self)) {
			ci.cancel();
		}
	}

	@ModifyReturnValue(method = "isPushedByFluid", at = @At("RETURN"))
	private boolean tf$isPushedByFluid(boolean original) {
		if (NeoHooks.overrides(this, "isPushedByFluid", 1)) {
			return ((Entity) (Object) this).isPushedByFluid(FluidType.WATER);
		}
		return original;
	}
}
