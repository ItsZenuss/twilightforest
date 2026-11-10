package twilightforest.mixin.event;

import net.minecraft.world.Container;
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

/** Fabric port: fires GrindstoneEvent.OnPlaceItem before vanilla computes the grindstone result. */
@Mixin(GrindstoneMenu.class)
public abstract class GrindstoneMenuEventMixin {
	@Shadow
	@Final
	private Container resultSlots;
	@Shadow
	@Final
	Container repairSlots;

	@Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
	private void tf$onPlace(CallbackInfo ci) {
		ItemStack top = this.repairSlots.getItem(0);
		ItemStack bottom = this.repairSlots.getItem(1);
		if (top.isEmpty() && bottom.isEmpty()) return;
		GrindstoneEvent.OnPlaceItem event = NeoForge.EVENT_BUS.post(new GrindstoneEvent.OnPlaceItem(top, bottom));
		if (event.isCanceled()) {
			this.resultSlots.setItem(0, ItemStack.EMPTY);
			((GrindstoneMenu) (Object) this).broadcastChanges();
			ci.cancel();
		} else if (!event.getOutput().isEmpty()) {
			this.resultSlots.setItem(0, event.getOutput());
			((GrindstoneMenu) (Object) this).broadcastChanges();
			ci.cancel();
		}
	}
}
