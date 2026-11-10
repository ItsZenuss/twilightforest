package twilightforest.mixin.ext;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.fabric.NeoHooks;

/**
 * Fabric port: IItemExtension#isGazeDisguise.
 */
@Mixin(EnderMan.class)
public abstract class EnderManMixin {
	@Inject(method = "isBeingStaredBy", at = @At("HEAD"), cancellable = true)
	private void tf$gazeDisguise(Player player, CallbackInfoReturnable<Boolean> cir) {
		ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
		if (NeoHooks.overrides(head.getItem(), "isGazeDisguise", 3) && head.getItem().isGazeDisguise(head, player, (EnderMan) (Object) this)) {
			cir.setReturnValue(false);
		}
	}
}
