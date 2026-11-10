package twilightforest.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HumanoidArm;
import twilightforest.port.neoforged.neoforge.client.event.RenderArmEvent;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Fabric port: fires RenderArmEvent after the first-person arm is rendered. */
@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererEventMixin {
	@Inject(method = "renderRightHand", at = @At("TAIL"))
	private void tf$rightArm(PoseStack poseStack, SubmitNodeCollector collector, int light, Identifier skin, boolean sleeve, CallbackInfo ci) {
		this.tf$fire(HumanoidArm.RIGHT, poseStack, collector, light);
	}

	@Inject(method = "renderLeftHand", at = @At("TAIL"))
	private void tf$leftArm(PoseStack poseStack, SubmitNodeCollector collector, int light, Identifier skin, boolean sleeve, CallbackInfo ci) {
		this.tf$fire(HumanoidArm.LEFT, poseStack, collector, light);
	}

	private void tf$fire(HumanoidArm arm, PoseStack poseStack, SubmitNodeCollector collector, int light) {
		if (Minecraft.getInstance().player != null) {
			NeoForge.EVENT_BUS.post(new RenderArmEvent(Minecraft.getInstance().player, arm, collector, poseStack, light));
		}
	}
}
