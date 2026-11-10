package twilightforest.mixin.compat;

import org.spongepowered.asm.mixin.Mixin;

/**
 * Fabric port: makes the NeoForge extension interface that Loom injects at compile time exist at runtime.
 */
@Mixin(net.minecraft.world.level.Level.class)
public abstract class LevelExtensionMixin implements twilightforest.port.neoforged.neoforge.common.extensions.ILevelExtension {
}
