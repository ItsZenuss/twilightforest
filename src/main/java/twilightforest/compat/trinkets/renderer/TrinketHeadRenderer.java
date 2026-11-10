package twilightforest.compat.trinkets.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class TrinketHeadRenderer implements TrinketRenderer {

	@Override
	public void submit(ItemStack stack, TrinketSlotAccess slot, EntityModel<? extends LivingEntityRenderState> entityModel, PoseStack matrixStack, SubmitNodeCollector collector, int light, LivingEntityRenderState state, float yRotation, float xRotation) {
		if (entityModel instanceof HeadedModel headModel) {
			matrixStack.pushPose();
			headModel.getHead().translateAndRotate(matrixStack);
			matrixStack.translate(0.0D, -0.25D, 0.0D);
			matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
			matrixStack.scale(0.625F, -0.625F, -0.625F);
			TrinketItemRenderer.render(slot, stack, ItemDisplayContext.HEAD, matrixStack, collector, light);
			matrixStack.popPose();
		}
	}
}
