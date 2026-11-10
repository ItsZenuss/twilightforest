package twilightforest.mixin.ext;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.fabric.CustomMapData;

/**
 * Fabric port: MapItem#getSavedData(ItemStack, Level) returns Twilight Forest map data for TF map items, like
 * NeoForge's getCustomMapData hook.
 */
@Mixin(MapItem.class)
public abstract class MapItemMixin {
	@Inject(method = "getSavedData(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;", at = @At("HEAD"), cancellable = true)
	private static void tf$customMapData(ItemStack stack, Level level, CallbackInfoReturnable<MapItemSavedData> cir) {
		MapItemSavedData custom = CustomMapData.get(stack, level);
		if (custom != null) cir.setReturnValue(custom);
	}
}
