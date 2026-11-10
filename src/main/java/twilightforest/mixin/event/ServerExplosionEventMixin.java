package twilightforest.mixin.event;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ServerExplosion;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.level.ExplosionEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

/** Fabric port: fires ExplosionEvent.Detonate with the entities about to be hurt; listeners may remove entries. */
@Mixin(ServerExplosion.class)
public abstract class ServerExplosionEventMixin {
	@Shadow
	@Final
	private ServerLevel level;

	@ModifyExpressionValue(method = "hurtEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"))
	private List<Entity> tf$detonate(List<Entity> entities) {
		List<Entity> mutable = new ArrayList<>(entities);
		NeoForge.EVENT_BUS.post(new ExplosionEvent.Detonate(this.level, (ServerExplosion) (Object) this, mutable));
		return mutable;
	}
}
