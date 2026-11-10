package twilightforest.mixin.compat;

import org.spongepowered.asm.mixin.Mixin;

/**
 * Fabric port: makes the NeoForge extension interface that Loom injects at compile time exist at runtime.
 */
@Mixin(net.minecraft.world.item.Item.class)
public abstract class ItemExtensionMixin implements twilightforest.port.neoforged.neoforge.common.extensions.IItemExtension {
}
