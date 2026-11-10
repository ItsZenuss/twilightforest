package twilightforest.mixin.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.tick.EntityTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

/**
 * Fabric port: fires NeoForge's EntityTickEvent around each guarded (non-passenger) entity tick, on both sides.
 */
@Mixin(Level.class)
public abstract class LevelEntityTickMixin {
	@Inject(method = "guardEntityTick", at = @At("HEAD"))
	private <T extends Entity> void tf$preEntityTick(Consumer<T> tick, T entity, CallbackInfo ci) {
		NeoForge.EVENT_BUS.post(new EntityTickEvent.Pre(entity));
	}

	@Inject(method = "guardEntityTick", at = @At("TAIL"))
	private <T extends Entity> void tf$postEntityTick(Consumer<T> tick, T entity, CallbackInfo ci) {
		NeoForge.EVENT_BUS.post(new EntityTickEvent.Post(entity));
	}
}
