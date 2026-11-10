package twilightforest.mixin.event;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.GrindstoneEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.mixin.event.GrindstoneMenuAccessor;

/** Fabric port: fires GrindstoneEvent.OnTakeItem when the grindstone output is taken. */
@Mixin(targets = "net.minecraft.world.inventory.GrindstoneMenu$4")
public abstract class GrindstoneResultSlotEventMixin {
	@Shadow
	@Final
	GrindstoneMenu this$0;

	@Inject(method = "onTake", at = @At("HEAD"))
	private void tf$onTake(Player player, ItemStack carried, CallbackInfo ci) {
		Container slots = ((GrindstoneMenuAccessor) this.this$0).twilightforest$repairSlots();
		NeoForge.EVENT_BUS.post(new GrindstoneEvent.OnTakeItem(slots.getItem(0), slots.getItem(1), player));
	}
}
