package twilightforest.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.CustomMapData;

/**
 * Fabric port: item frames render Twilight Forest maps using TF map data.
 */
@Mixin(ItemFrameRenderer.class)
public abstract class ItemFrameMapMixin {
	@WrapOperation(method = "extractRenderState(Lnet/minecraft/world/entity/decoration/ItemFrame;Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;F)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getMapData(Lnet/minecraft/world/level/saveddata/maps/MapId;)Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;"))
	private MapItemSavedData tf$customMap(Level level, MapId id, Operation<MapItemSavedData> original, @Local(argsOnly = true) ItemFrame frame) {
		MapItemSavedData custom = CustomMapData.get(frame.getItem(), level);
		return custom != null ? custom : original.call(level, id);
	}
}
