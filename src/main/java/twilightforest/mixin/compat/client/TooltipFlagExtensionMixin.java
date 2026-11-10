package twilightforest.mixin.compat.client;

import org.spongepowered.asm.mixin.Mixin;

/**
 * Fabric port: makes the NeoForge extension interface that Loom injects at compile time exist at runtime.
 */
@Mixin(net.minecraft.world.item.TooltipFlag.class)
public interface TooltipFlagExtensionMixin extends twilightforest.port.neoforged.neoforge.client.extensions.ITooltipFlagExtension {
}
