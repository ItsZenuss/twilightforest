package twilightforest.mixin.client;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import twilightforest.port.neoforged.neoforge.client.event.InputEvent;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Fabric port: fires InputEvent.Key for every key press/release/repeat. */
@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerEventMixin {
	@Inject(method = "keyPress", at = @At("TAIL"))
	private void tf$key(long window, int action, KeyEvent event, CallbackInfo ci) {
		NeoForge.EVENT_BUS.post(new InputEvent.Key(event, action));
	}
}
