package twilightforest.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.HitResult;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Fabric port: fires PlayerInteractEvent.LeftClickEmpty when attacking air. */
@Mixin(Minecraft.class)
public abstract class MinecraftEventMixin {
	@Inject(method = "startAttack", at = @At("HEAD"))
	private void tf$leftClickEmpty(CallbackInfoReturnable<Boolean> cir) {
		Minecraft self = (Minecraft) (Object) this;
		if (self.player != null && (self.hitResult == null || self.hitResult.getType() == HitResult.Type.MISS)) {
			NeoForge.EVENT_BUS.post(new PlayerInteractEvent.LeftClickEmpty(self.player));
		}
	}
}
