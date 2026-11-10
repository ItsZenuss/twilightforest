package twilightforest.port.neoforged.neoforge.transfer.fluid;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.minecraft.world.item.ItemStack;
import twilightforest.port.neoforged.neoforge.transfer.fabric.FabricResourceHandlers;

/**
 * Fabric port: reads item fluid contents through Fabric's FluidStorage.ITEM lookup.
 */
public final class FluidUtil {
	private FluidUtil() {}

	public static FluidResource getFirstStackContained(ItemStack stack) {
		if (stack.isEmpty()) return FluidResource.EMPTY;
		Storage<FluidVariant> storage = ContainerItemContext.withConstant(stack).find(FluidStorage.ITEM);
		if (storage == null) return FluidResource.EMPTY;
		for (StorageView<FluidVariant> view : storage.nonEmptyViews()) {
			return FabricResourceHandlers.fromVariant(view.getResource());
		}
		return FluidResource.EMPTY;
	}
}
