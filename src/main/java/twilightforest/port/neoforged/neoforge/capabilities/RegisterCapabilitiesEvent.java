package twilightforest.port.neoforged.neoforge.capabilities;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import twilightforest.port.neoforged.neoforge.transfer.ResourceHandler;
import twilightforest.port.neoforged.neoforge.transfer.fabric.FabricResourceHandlers;
import twilightforest.port.neoforged.neoforge.transfer.item.ItemResource;

/**
 * Fabric port: block and block entity capability providers are kept on the {@link BlockCapability} (for Twilight
 * Forest's own lookups), and item handlers are also exposed to Fabric's transfer API for hoppers and other mods.
 */
public class RegisterCapabilitiesEvent {
	@FunctionalInterface
	public interface BlockEntityCapabilityProvider<BE extends BlockEntity, T, C> {
		T getCapability(BE blockEntity, C context);
	}

	@SuppressWarnings("unchecked")
	public <T, C> void registerBlock(BlockCapability<T, C> capability, Object provider, Block... blocks) {
		IBlockCapabilityProvider<T, C> typed = (IBlockCapabilityProvider<T, C>) provider;
		for (Block block : blocks) capability.registerBlock(block, typed);
		if (capability == Capabilities.Item.BLOCK) {
			ItemStorage.SIDED.registerForBlocks((level, pos, state, be, side) -> {
				Object handler = typed.getCapability(level, pos, state, be, (C) side);
				return handler == null ? null : FabricResourceHandlers.itemStorage((ResourceHandler<ItemResource>) handler);
			}, blocks);
		}
	}

	@SuppressWarnings("unchecked")
	public <BE extends BlockEntity, T, C> void registerBlockEntity(BlockCapability<T, C> capability, BlockEntityType<BE> type, BlockEntityCapabilityProvider<BE, T, C> provider) {
		capability.registerBlockEntity(type, (level, pos, state, be, context) -> be == null ? null : provider.getCapability((BE) be, context));
		if (capability == Capabilities.Item.BLOCK) {
			ItemStorage.SIDED.registerForBlockEntity((be, side) -> {
				Object handler = provider.getCapability(be, (C) side);
				return handler == null ? null : FabricResourceHandlers.itemStorage((ResourceHandler<ItemResource>) handler);
			}, type);
		}
	}

	@FunctionalInterface
	public interface ItemCapabilityProvider<T, C> {
		T getCapability(net.minecraft.world.item.ItemStack stack, C context);
	}

	public <T, C> void registerItem(ItemCapability<T, C> capability, ItemCapabilityProvider<T, C> provider, Object... items) {
		// Twilight Forest registers no item capabilities outside Curios compat, which is ported to Trinkets callbacks instead.
	}
}
