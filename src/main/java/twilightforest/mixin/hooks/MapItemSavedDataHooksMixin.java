package twilightforest.mixin.hooks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.asmhooks.MapHooks;

/**
 * Fabric port of the NeoForge coremod transformer {@code twilightforest.asm.transformers.map.UpdateMapsInGogglesTransformer}.
 */
@Mixin(MapItemSavedData.class)
public abstract class MapItemSavedDataHooksMixin {
	@ModifyExpressionValue(method = "tickCarriedBy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;contains(Ljava/util/function/Predicate;)Z"))
	private boolean tf$goggles(boolean original, Player player, ItemStack stack, ItemFrame frame) {
		return MapHooks.updateMapsInGoggles(original, stack, player);
	}
}
