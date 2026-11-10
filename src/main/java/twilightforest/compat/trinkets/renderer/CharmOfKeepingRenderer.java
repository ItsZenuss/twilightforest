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

public class CharmOfKeepingRenderer implements TrinketRenderer {

	@Override
	public void submit(ItemStack stack, TrinketSlotAccess slot, EntityModel<? extends LivingEntityRenderState> entityModel, PoseStack ms, SubmitNodeCollector buffer, int light, LivingEntityRenderState state, float yRotation, float xRotation) {
		if (entityModel instanceof HumanoidModel<?> model) {
			ms.pushPose();
			model.rightLeg.translateAndRotate(ms);
			ms.translate(-0.0D, 0.15D, -0.15D);
			ms.mulPose(Axis.YP.rotationDegrees(0.0F));
			ms.scale(0.3F, -0.3F, -0.3F);
			TrinketItemRenderer.render(slot, stack, ItemDisplayContext.FIXED, ms, buffer, light);
			ms.popPose();
		}
	}
}
