package twilightforest.mixin.ext;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.fabric.MultipartEntities;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Fabric port: entity queries include the parts of NeoForge-style multipart entities (Hydra heads, Naga segments...),
 * so they can be hit, targeted and collided with, as on NeoForge.
 */
@Mixin(Level.class)
public abstract class LevelMultipartMixin {
	@ModifyReturnValue(method = "getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;", at = @At("RETURN"))
	private List<Entity> tf$addParts(List<Entity> result, Entity except, AABB area, Predicate<? super Entity> filter) {
		MultipartEntities.collectParts((Level) (Object) this, except, area, Function.identity(), filter, result);
		return result;
	}

	@Inject(method = "getEntities(Lnet/minecraft/world/level/entity/EntityTypeTest;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;Ljava/util/List;I)V", at = @At("TAIL"))
	private <T extends Entity> void tf$addTypedParts(EntityTypeTest<Entity, T> test, AABB area, Predicate<? super T> filter, List<? super T> out, int limit, CallbackInfo ci) {
		if (out.size() < limit) {
			MultipartEntities.collectParts((Level) (Object) this, null, area, test::tryCast, filter, out);
		}
	}
}
