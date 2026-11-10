package twilightforest.mixin.hooks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import twilightforest.asmhooks.ArmorHooks;
import twilightforest.asmhooks.BlockHooks;
import twilightforest.asmhooks.EntityHooks;

/**
 * Fabric port of the NeoForge coremod transformer {@code twilightforest.asm.transformers.armor.ArmorVisibilityRenderingTransformer, entity.WaterWalkTransformer, entity.WaterCollisionTransformer, block.UnrestrainedFrictionTransformer}.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityHooksMixin {
	@ModifyVariable(method = "getVisibilityPercent", at = @At("STORE"), ordinal = 0)
	private float tf$armorVisibility(float coverage) {
		return ArmorHooks.modifyArmorVisibility(coverage, (LivingEntity) (Object) this);
	}

	@ModifyReturnValue(method = "canStandOnFluid", at = @At("RETURN"))
	private boolean tf$waterWalk(boolean original, FluidState fluid) {
		return EntityHooks.processWaterWalking(original, (LivingEntity) (Object) this, fluid);
	}

	@ModifyReturnValue(method = "getLiquidCollisionShape", at = @At("RETURN"))
	private VoxelShape tf$liquidCollision(VoxelShape original) {
		return EntityHooks.processLiquidCollisionShape(original, (LivingEntity) (Object) this);
	}

	// NeoForge applied this inside IBlockExtension#getFriction; on Fabric, wrap vanilla's friction lookup instead.
	@ModifyExpressionValue(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getFriction()F"))
	private float tf$unrestrainedFriction(float friction) {
		return BlockHooks.resetBlockFrictionWithUnrestrained(friction, (LivingEntity) (Object) this);
	}
}
