package twilightforest.mixin.ext;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.fabric.MultipartEntities;

/**
 * Fabric port: resolves multipart entity part ids (used by attack/interact packets), like vanilla does for dragon parts.
 */
@Mixin(ServerLevel.class)
public abstract class ServerLevelMultipartMixin {
	@ModifyReturnValue(method = "getEntityOrPart", at = @At("RETURN"))
	private Entity tf$findPart(Entity original, int id) {
		return original != null ? original : MultipartEntities.findPart((ServerLevel) (Object) this, id);
	}
}
