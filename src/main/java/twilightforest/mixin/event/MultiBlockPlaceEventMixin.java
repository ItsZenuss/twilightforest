package twilightforest.mixin.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.common.util.BlockSnapshot;
import twilightforest.port.neoforged.neoforge.event.level.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/** Fabric port: fires BlockEvent.EntityMultiPlaceEvent for beds and double-high blocks (doors, tall plants). */
@Mixin({BedItem.class, DoubleHighBlockItem.class})
public abstract class MultiBlockPlaceEventMixin {
	@Inject(method = "placeBlock", at = @At("HEAD"), cancellable = true)
	private void tf$multiPlace(BlockPlaceContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (context.getPlayer() == null) return;
		BlockPos pos = context.getClickedPos();
		BlockPos other = (Object) this instanceof BedItem ? pos.relative(state.getValue(BedBlock.FACING)) : pos.above();
		List<BlockSnapshot> snapshots = List.of(
			new BlockSnapshot(pos, context.getLevel().getBlockState(pos)),
			new BlockSnapshot(other, context.getLevel().getBlockState(other)));
		BlockState against = context.getLevel().getBlockState(pos.relative(context.getClickedFace().getOpposite()));
		if (NeoForge.EVENT_BUS.post(new BlockEvent.EntityMultiPlaceEvent(snapshots, against, context.getPlayer())).isCanceled()) {
			cir.setReturnValue(false);
		}
	}
}
