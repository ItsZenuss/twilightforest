package twilightforest.port.neoforged.neoforge.capabilities;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.port.neoforged.neoforge.transfer.fabric.FabricResourceHandlers;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Fabric port of NeoForge's BlockCapability. Providers registered through {@link RegisterCapabilitiesEvent} are
 * consulted first; item and fluid capabilities otherwise fall back to Fabric's ItemStorage/FluidStorage lookups, so
 * vanilla and other mods' containers work.
 */
public class BlockCapability<T, C> {
	private final String name;
	private final Map<Block, IBlockCapabilityProvider<T, C>> blockProviders = new IdentityHashMap<>();
	private final Map<BlockEntityType<?>, IBlockCapabilityProvider<T, C>> blockEntityProviders = new IdentityHashMap<>();

	public BlockCapability(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	void registerBlock(Block block, IBlockCapabilityProvider<T, C> provider) {
		this.blockProviders.put(block, provider);
	}

	void registerBlockEntity(BlockEntityType<?> type, IBlockCapabilityProvider<T, C> provider) {
		this.blockEntityProviders.put(type, provider);
	}

	@Nullable
	public T getCapability(Level level, BlockPos pos, @Nullable C context) {
		BlockState state = level.getBlockState(pos);
		return this.getCapability(level, pos, state, state.hasBlockEntity() ? level.getBlockEntity(pos) : null, context);
	}

	@Nullable
	@SuppressWarnings("unchecked")
	public T getCapability(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity blockEntity, @Nullable C context) {
		if (state == null) state = level.getBlockState(pos);
		if (blockEntity == null && state.hasBlockEntity()) blockEntity = level.getBlockEntity(pos);
		IBlockCapabilityProvider<T, C> provider = blockEntity != null ? this.blockEntityProviders.get(blockEntity.getType()) : null;
		if (provider == null) provider = this.blockProviders.get(state.getBlock());
		if (provider != null) {
			return provider.getCapability(level, pos, state, blockEntity, context);
		}
		Direction side = context instanceof Direction d ? d : null;
		if (this == Capabilities.Item.BLOCK) {
			return (T) FabricResourceHandlers.items(ItemStorage.SIDED.find(level, pos, state, blockEntity, side));
		}
		if (this == Capabilities.Fluid.BLOCK) {
			return (T) FabricResourceHandlers.fluids(FluidStorage.SIDED.find(level, pos, state, blockEntity, side));
		}
		return null;
	}
}
