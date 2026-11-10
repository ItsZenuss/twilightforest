package twilightforest.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.world.BossEvent;
import twilightforest.port.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Fabric port: fires CustomizeGuiOverlayEvent.BossEventProgress per boss bar; canceling skips vanilla's bar. */
@Mixin(BossHealthOverlay.class)
public abstract class BossHealthOverlayEventMixin {
	@WrapOperation(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/BossHealthOverlay;extractBar(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IILnet/minecraft/world/BossEvent;)V"))
	private void tf$bossBar(BossHealthOverlay overlay, GuiGraphicsExtractor graphics, int x, int y, BossEvent bossEvent, Operation<Void> original) {
		if (!NeoForge.EVENT_BUS.post(new CustomizeGuiOverlayEvent.BossEventProgress(bossEvent, graphics, x, y)).isCanceled()) {
			original.call(overlay, graphics, x, y, bossEvent);
		}
	}
}
