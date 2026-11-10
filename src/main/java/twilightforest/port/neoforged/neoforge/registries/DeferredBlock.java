package twilightforest.port.neoforged.neoforge.registries;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class DeferredBlock<T extends Block> extends DeferredHolder<Block, T> implements ItemLike {

	public DeferredBlock(ResourceKey<Block> key, @org.jetbrains.annotations.Nullable Supplier<? extends T> supplier) {
		super(key, supplier);
	}

	@Override
	public Item asItem() {
		return get().asItem();
	}

	public BlockState defaultBlockState() {
		return get().defaultBlockState();
	}

	public net.minecraft.world.item.ItemStack toStack() {
		return new net.minecraft.world.item.ItemStack(this);
	}

	public net.minecraft.world.item.ItemStack toStack(int count) {
		return new net.minecraft.world.item.ItemStack(this, count);
	}
}
