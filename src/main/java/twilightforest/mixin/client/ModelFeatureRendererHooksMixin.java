package twilightforest.mixin.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asmhooks.RenderHooks;

/**
 * Fabric port of the NeoForge coremod transformer {@code twilightforest.asm.transformers.render.HideHeadUnderTrophyTransformer}.
 */
@Mixin(ModelFeatureRenderer.class)
public abstract class ModelFeatureRendererHooksMixin {
	@Inject(method = "renderModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/Model;setupAnim(Ljava/lang/Object;)V", shift = At.Shift.AFTER))
	private <S> void tf$hideHead(SubmitNodeStorage.ModelSubmit<S> submit, RenderType type, VertexConsumer consumer, OutlineBufferSource outline, MultiBufferSource.BufferSource buffers, CallbackInfo ci) {
		RenderHooks.applyHeadVisibility(submit);
	}

	@Inject(method = "renderModel", at = @At("TAIL"))
	private <S> void tf$restoreHead(SubmitNodeStorage.ModelSubmit<S> submit, RenderType type, VertexConsumer consumer, OutlineBufferSource outline, MultiBufferSource.BufferSource buffers, CallbackInfo ci) {
		RenderHooks.restoreHeadVisibility(submit);
	}
}
