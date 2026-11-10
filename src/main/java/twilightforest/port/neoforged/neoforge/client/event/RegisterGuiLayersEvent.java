package twilightforest.port.neoforged.neoforge.client.event;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import twilightforest.port.neoforged.bus.api.Event;

/**
 * Fabric port: GUI layers become Fabric HUD elements. Layer ids come from
 * {@link twilightforest.port.neoforged.neoforge.client.gui.VanillaGuiLayers}, which maps to Fabric's vanilla HUD element ids.
 */
public class RegisterGuiLayersEvent extends Event {
	public void registerAbove(Identifier above, Identifier id, Layer layer) {
		HudElementRegistry.attachElementAfter(above, id, layer::render);
	}

	public void registerBelow(Identifier below, Identifier id, Layer layer) {
		HudElementRegistry.attachElementBefore(below, id, layer::render);
	}

	public void register(Identifier id, Layer layer) {
		HudElementRegistry.addLast(id, layer::render);
	}

	public void registerAboveAll(Identifier id, Layer layer) {
		HudElementRegistry.addLast(id, layer::render);
	}

	public void registerBelowAll(Identifier id, Layer layer) {
		HudElementRegistry.addFirst(id, layer::render);
	}

	@FunctionalInterface
	public interface Layer {
		void render(GuiGraphicsExtractor graphics, DeltaTracker partialTicks);
	}
}
