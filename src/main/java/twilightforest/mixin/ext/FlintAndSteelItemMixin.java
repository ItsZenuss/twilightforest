package twilightforest.mixin.ext;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.port.neoforged.neoforge.common.ItemAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.fabric.NeoHooks;

/**
 * Fabric port: IBlockExtension#getToolModifiedState with the FIRESTARTER_LIGHT ability (flint and steel).
 */
@Mixin(FlintAndSteelItem.class)
public abstract class FlintAndSteelItemMixin {
	@Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
	private void tf$firestarterLight(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = level.getBlockState(pos);
		if (!NeoHooks.overrides(state.getBlock(), "getToolModifiedState", 4)) return;
		BlockState lit = state.getBlock().getToolModifiedState(state, context, ItemAbilities.FIRESTARTER_LIGHT, false);
		if (lit == null) return;
		level.playSound(context.getPlayer(), pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
		level.setBlock(pos, lit, Block.UPDATE_ALL_IMMEDIATE);
		if (context.getPlayer() != null) context.getItemInHand().hurtAndBreak(1, context.getPlayer(), context.getHand().asEquipmentSlot());
		cir.setReturnValue(InteractionResult.SUCCESS);
	}
}
