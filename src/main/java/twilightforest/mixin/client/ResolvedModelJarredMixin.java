package twilightforest.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.client.fabric.JarredTransformHolder;

/**
 * Fabric port: carries a model's (or its nearest parent's) jarred transform into the resolved transforms.
 */
@Mixin(ResolvedModel.class)
public interface ResolvedModelJarredMixin {
	@ModifyReturnValue(method = "findTopTransforms", at = @At("RETURN"))
	private static ItemTransforms twilightforest$resolveJarred(ItemTransforms resolved, ResolvedModel model) {
		for (ResolvedModel current = model; current != null; current = current.parent()) {
			ItemTransforms own = current.wrapped().transforms();
			if (own != null && ((JarredTransformHolder) (Object) own).twilightforest$getJarred() != null) {
				ItemTransforms copy = new ItemTransforms(resolved.thirdPersonLeftHand(), resolved.thirdPersonRightHand(), resolved.firstPersonLeftHand(),
					resolved.firstPersonRightHand(), resolved.head(), resolved.gui(), resolved.ground(), resolved.fixed(), resolved.fixedFromBottom());
				((JarredTransformHolder) (Object) copy).twilightforest$setJarred(((JarredTransformHolder) (Object) own).twilightforest$getJarred());
				return copy;
			}
		}
		return resolved;
	}
}
