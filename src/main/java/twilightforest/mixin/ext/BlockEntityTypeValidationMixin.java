package twilightforest.mixin.ext;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Fabric port: validate a block entity's block against {@link BlockEntity#getType()} like NeoForge does, so block
 * entities that subclass a vanilla one (e.g. the Cinder Furnace extending FurnaceBlockEntity) but report their own
 * type are accepted.
 */
@Mixin(BlockEntity.class)
public abstract class BlockEntityTypeValidationMixin {
	@ModifyExpressionValue(method = "isValidBlockState", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/entity/BlockEntity;type:Lnet/minecraft/world/level/block/entity/BlockEntityType;"))
	private BlockEntityType<?> tf$useGetType(BlockEntityType<?> type) {
		BlockEntityType<?> actual = ((BlockEntity) (Object) this).getType();
		return actual != null ? actual : type;
	}
}
