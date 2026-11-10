package twilightforest.mixin.compat;

import org.spongepowered.asm.mixin.Mixin;

/**
 * Fabric port: makes the NeoForge extension interface that Loom injects at compile time exist at runtime.
 */
@Mixin(net.minecraft.world.level.block.state.BlockState.class)
public abstract class BlockStateExtensionMixin implements twilightforest.port.neoforged.neoforge.common.extensions.IBlockStateExtension {
}
