package twilightforest.mixin.compat;

import org.spongepowered.asm.mixin.Mixin;

/**
 * Fabric port: makes the NeoForge extension interface that Loom injects at compile time exist at runtime.
 */
@Mixin(net.minecraft.world.item.crafting.RecipeManager.class)
public abstract class RecipeManagerExtensionMixin implements twilightforest.port.neoforged.neoforge.common.extensions.IRecipeManagerExtension {
}
