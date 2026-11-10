package twilightforest.compat.trinkets.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import eu.pb4.trinkets.api.TrinketSlotAccess;
import org.jspecify.annotations.Nullable;

/**
 * Shared helper for drawing a worn trinket as an item model.
 */
final class TrinketItemRenderer {
	@Nullable
	private static ItemInHandRenderer renderer;

	private TrinketItemRenderer() {}

	static void render(TrinketSlotAccess slot, ItemStack stack, ItemDisplayContext context, PoseStack poseStack, SubmitNodeCollector collector, int light) {
		LivingEntity entity = slot.inventory().getAttachment().getEntity();
		if (renderer == null) {
			Minecraft minecraft = Minecraft.getInstance();
			renderer = new ItemInHandRenderer(minecraft, minecraft.getEntityRenderDispatcher(), minecraft.getItemModelResolver());
		}
		renderer.renderItem(entity, stack, context, poseStack, collector, light);
	}
}
