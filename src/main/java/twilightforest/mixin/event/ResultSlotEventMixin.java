package twilightforest.mixin.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Fabric port: fires PlayerEvent.ItemCraftedEvent when a crafting result is taken. */
@Mixin(ResultSlot.class)
public abstract class ResultSlotEventMixin {
	@Shadow
	@Final
	private CraftingContainer craftSlots;
	@Shadow
	@Final
	private Player player;

	@Inject(method = "checkTakeAchievements", at = @At("HEAD"))
	private void tf$crafted(ItemStack stack, CallbackInfo ci) {
		NeoForge.EVENT_BUS.post(new PlayerEvent.ItemCraftedEvent(this.player, stack, this.craftSlots));
	}
}
