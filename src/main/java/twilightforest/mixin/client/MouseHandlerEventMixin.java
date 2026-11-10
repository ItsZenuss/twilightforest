package twilightforest.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import twilightforest.port.neoforged.neoforge.client.event.CalculatePlayerTurnEvent;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/** Fabric port: fires CalculatePlayerTurnEvent so listeners can change the mouse sensitivity for this turn. */
@Mixin(MouseHandler.class)
public abstract class MouseHandlerEventMixin {
	@Shadow
	@Final
	private Minecraft minecraft;

	@ModifyExpressionValue(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;", ordinal = 0))
	private Object tf$sensitivity(Object sensitivity) {
		CalculatePlayerTurnEvent event = NeoForge.EVENT_BUS.post(new CalculatePlayerTurnEvent(this.minecraft.options.smoothCamera, (Double) sensitivity));
		return event.getMouseSensitivity();
	}
}
