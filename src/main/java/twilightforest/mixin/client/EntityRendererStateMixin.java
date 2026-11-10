package twilightforest.mixin.client;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import twilightforest.port.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fabric port: runs NeoForge-style render-state modifiers after a renderer fills its render state.
 */
@Mixin(EntityRenderer.class)
public abstract class EntityRendererStateMixin {
	@Inject(method = "extractRenderState", at = @At("TAIL"))
	private void tf$applyModifiers(Entity entity, EntityRenderState state, float partialTick, CallbackInfo ci) {
		for (RegisterRenderStateModifiersEvent.EntityModifier modifier : RegisterRenderStateModifiersEvent.getEntityModifiers()) {
			if (modifier.rendererClass().isInstance(this)) {
				modifier.consumer().accept(entity, state);
			}
		}
	}
}
