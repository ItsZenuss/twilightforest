package twilightforest.mixin.event;

import net.minecraft.world.entity.player.Player;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fabric port: fires NeoForge's PlayerTickEvent on both sides.
 */
@Mixin(Player.class)
public abstract class PlayerTickMixin {
	@Inject(method = "tick", at = @At("HEAD"))
	private void tf$preTick(CallbackInfo ci) {
		NeoForge.EVENT_BUS.post(new PlayerTickEvent.Pre((Player) (Object) this));
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void tf$postTick(CallbackInfo ci) {
		NeoForge.EVENT_BUS.post(new PlayerTickEvent.Post((Player) (Object) this));
	}
}
