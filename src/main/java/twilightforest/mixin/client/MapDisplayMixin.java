package twilightforest.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.CustomMapData;

/**
 * Fabric port: first-person map rendering uses Twilight Forest map data for TF map items.
 */
@Mixin(ItemInHandRenderer.class)
public abstract class MapDisplayMixin {
	@WrapOperation(method = "renderMap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/MapItem;getSavedData(Lnet/minecraft/world/level/saveddata/maps/MapId;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;"))
	private MapItemSavedData tf$customMap(MapId id, Level level, Operation<MapItemSavedData> original, @Local(argsOnly = true) ItemStack stack) {
		MapItemSavedData custom = CustomMapData.get(stack, level);
		return custom != null ? custom : original.call(id, level);
	}
}
