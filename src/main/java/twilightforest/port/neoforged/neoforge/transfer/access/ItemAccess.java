package twilightforest.port.neoforged.neoforge.transfer.access;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import twilightforest.port.neoforged.neoforge.capabilities.Capabilities;
import twilightforest.port.neoforged.neoforge.capabilities.ItemCapability;
import twilightforest.port.neoforged.neoforge.transfer.fabric.FabricResourceHandlers;

/**
 * Fabric port: item capabilities come from Fabric's item API lookups with a player-hand container context (which
 * handles bucket exchanges and creative mode).
 */
public class ItemAccess {
	private final ContainerItemContext context;

	private ItemAccess(ContainerItemContext context) {
		this.context = context;
	}

	public static ItemAccess forPlayerInteraction(Player player, InteractionHand hand) {
		return new ItemAccess(ContainerItemContext.forPlayerInteraction(player, hand));
	}

	public ItemAccess oneByOne() {
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T, C> T getCapability(ItemCapability<T, C> cap) {
		if (cap == Capabilities.Fluid.ITEM) {
			return (T) FabricResourceHandlers.fluids(this.context.find(FluidStorage.ITEM));
		}
		if (cap == Capabilities.Item.ITEM) {
			return (T) FabricResourceHandlers.items(this.context.find(ItemStorage.ITEM));
		}
		return null;
	}
}
