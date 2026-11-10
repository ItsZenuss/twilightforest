package twilightforest.port.neoforged.neoforge.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import twilightforest.port.neoforged.neoforge.transfer.ResourceHandler;
import twilightforest.port.neoforged.neoforge.transfer.fluid.FluidResource;
import twilightforest.port.neoforged.neoforge.transfer.item.ItemResource;
import twilightforest.port.neoforged.neoforge.transfer.transaction.TransactionContext;

public final class Capabilities {
	private Capabilities() {}

	public static final class Item {
		public static final BlockCapability<ResourceHandler<ItemResource>, Direction> BLOCK = new BlockCapability<>("item_block");
		public static final EntityCapability<ResourceHandler<ItemResource>, Direction> ENTITY_AUTOMATION = new EntityCapability<>("item_entity_automation");
		public static final ItemCapability<ResourceHandler<ItemResource>, Void> ITEM = new ItemCapability<>("item_item");
	}

	public static final class Fluid {
		public static final BlockCapability<ResourceHandler<FluidResource>, Direction> BLOCK = new BlockCapability<>("fluid_block");
		public static final ItemCapability<ResourceHandler<FluidResource>, Void> ITEM = new ItemCapability<>("fluid_item");
	}

	/**
	 * Fabric port: entities that are containers (chest minecarts, chest boats...) expose their inventory through
	 * Fabric's ContainerStorage.
	 */
	@SuppressWarnings("unchecked")
	public static <R, C> R getEntityCapability(Entity entity, EntityCapability<R, C> cap, C context) {
		if (cap == Item.ENTITY_AUTOMATION && entity instanceof Container container) {
			return (R) twilightforest.port.neoforged.neoforge.transfer.fabric.FabricResourceHandlers.items(
				net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage.of(container, context instanceof Direction d ? d : null));
		}
		return null;
	}
}
