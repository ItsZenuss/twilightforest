package twilightforest.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import twilightforest.port.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Fabric port: fires MovementInputUpdateEvent right after the player's movement input updates. */
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerEventMixin {
	@Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/ClientInput;tick()V", shift = At.Shift.AFTER))
	private void tf$movementInput(CallbackInfo ci) {
		LocalPlayer self = (LocalPlayer) (Object) this;
		NeoForge.EVENT_BUS.post(new MovementInputUpdateEvent(self, self.input));
	}
}
