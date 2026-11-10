package twilightforest.mixin.hooks;

import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.asmhooks.MultipartHooks;

/**
 * Fabric port of the NeoForge coremod transformer {@code twilightforest.asm.transformers.multipart.SendDirtyEntityDataTransformer}.
 */
@Mixin(ServerEntity.class)
public abstract class ServerEntityHooksMixin {
	@Shadow
	@Final
	private Entity entity;

	@Inject(method = "sendDirtyEntityData", at = @At("HEAD"))
	private void tf$multipartData(CallbackInfo ci) {
		MultipartHooks.sendDirtyEntityData(this.entity);
	}
}
