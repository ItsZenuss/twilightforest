package twilightforest.mixin.event;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.AnvilUpdateEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Fabric port: fires AnvilUpdateEvent before vanilla computes the anvil result. */
@Mixin(AnvilMenu.class)
public abstract class AnvilMenuEventMixin extends ItemCombinerMenu {
	@Shadow
	@Final
	private DataSlot cost;
	@Shadow
	private int repairItemCountCost;
	@Shadow
	private String itemName;

	private AnvilMenuEventMixin(MenuType<?> type, int id, Inventory inventory, ContainerLevelAccess access, ItemCombinerMenuSlotDefinition slots) {
		super(type, id, inventory, access, slots);
	}

	@Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
	private void tf$anvilUpdate(CallbackInfo ci) {
		ItemStack left = this.inputSlots.getItem(0);
		ItemStack right = this.inputSlots.getItem(1);
		if (left.isEmpty() || right.isEmpty()) return;
		AnvilUpdateEvent event = NeoForge.EVENT_BUS.post(new AnvilUpdateEvent(left, right, this.itemName, this.cost.get(), this.repairItemCountCost));
		if (event.isCanceled()) {
			this.resultSlots.setItem(0, ItemStack.EMPTY);
			this.cost.set(0);
			this.broadcastChanges();
			ci.cancel();
		} else if (!event.getOutput().isEmpty()) {
			this.resultSlots.setItem(0, event.getOutput());
			this.cost.set(event.getCost());
			this.repairItemCountCost = event.getMaterialCost();
			this.broadcastChanges();
			ci.cancel();
		}
	}
}
