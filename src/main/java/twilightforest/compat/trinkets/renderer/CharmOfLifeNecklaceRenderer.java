package twilightforest.compat.trinkets.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CharmOfLifeNecklaceRenderer implements TrinketRenderer {

	private final int necklaceColor;

	public CharmOfLifeNecklaceRenderer(int necklaceColor) {
		this.necklaceColor = necklaceColor;
	}

	@Override
	public void submit(ItemStack item, TrinketSlotAccess slot, EntityModel<? extends LivingEntityRenderState> entityModel, PoseStack stack, SubmitNodeCollector buffer, int light, LivingEntityRenderState state, float netHeadYaw, float headPitch) {
		if (entityModel instanceof HumanoidModel<?> model) {
			stack.pushPose();
			model.body.translateAndRotate(stack);
			stack.translate(-0.0D, 0.23D, -0.135D);
			stack.mulPose(Axis.YP.rotationDegrees(0.0F));
			stack.scale(-0.4F, -0.4F, 0.4F);
			TrinketItemRenderer.render(slot, item, ItemDisplayContext.FIXED, stack, buffer, light);
			stack.popPose();
		}
		// The necklace model itself (CharmOfLifeNecklaceModel tinted with necklaceColor) is disabled upstream too.
	}
}
