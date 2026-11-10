package twilightforest.mixin.compat.client;

import org.spongepowered.asm.mixin.Mixin;

/**
 * Fabric port: makes the NeoForge extension interface that Loom injects at compile time exist at runtime.
 */
@Mixin(net.minecraft.client.gui.GuiGraphicsExtractor.class)
public abstract class GuiGraphicsExtractorExtensionMixin implements twilightforest.port.neoforged.neoforge.client.extensions.IGuiGraphicsExtractorExtension {
}
