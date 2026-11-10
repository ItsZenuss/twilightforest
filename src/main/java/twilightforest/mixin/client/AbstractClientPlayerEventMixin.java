package twilightforest.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.player.AbstractClientPlayer;
import twilightforest.port.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Fabric port: fires ComputeFovModifierEvent. */
@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerEventMixin {
	@ModifyReturnValue(method = "getFieldOfViewModifier", at = @At("RETURN"))
	private float tf$fov(float original) {
		return NeoForge.EVENT_BUS.post(new ComputeFovModifierEvent((AbstractClientPlayer) (Object) this, original)).getNewFovModifier();
	}
}
