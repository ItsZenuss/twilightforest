package twilightforest.mixin.compat;

import net.minecraft.core.Holder;
import twilightforest.port.neoforged.neoforge.registries.datamaps.IHolderDataMapExtension;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Fabric port: adds NeoForge's data map accessor to every Holder at runtime.
 */
@Mixin(Holder.class)
public interface HolderExtensionMixin extends IHolderDataMapExtension {
}
