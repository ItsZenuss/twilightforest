package twilightforest.mixin.client;

import net.minecraft.client.resources.model.cuboid.ItemTransform;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.client.fabric.JarredTransformHolder;

/**
 * Fabric port: models can define a "twilightforest:jarred" display transform (NeoForge modded display contexts);
 * without one the jarred context falls back to the "fixed" transform, as NeoForge's fallback did.
 */
@Mixin(ItemTransforms.class)
public abstract class ItemTransformsJarredMixin implements JarredTransformHolder {
	@Shadow
	public abstract ItemTransform fixed();

	@Unique
	@Nullable
	private ItemTransform twilightforest$jarred;

	@Override
	@Nullable
	public ItemTransform twilightforest$getJarred() {
		return this.twilightforest$jarred;
	}

	@Override
	public void twilightforest$setJarred(@Nullable ItemTransform transform) {
		this.twilightforest$jarred = transform;
	}

	@Inject(method = "getTransform", at = @At("HEAD"), cancellable = true)
	private void twilightforest$jarredTransform(ItemDisplayContext context, CallbackInfoReturnable<ItemTransform> cir) {
		if (context == JarredTransformHolder.jarred()) {
			cir.setReturnValue(this.twilightforest$jarred != null ? this.twilightforest$jarred : this.fixed());
		}
	}
}
