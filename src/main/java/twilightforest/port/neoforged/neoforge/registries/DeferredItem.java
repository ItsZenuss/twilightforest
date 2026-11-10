package twilightforest.port.neoforged.neoforge.registries;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class DeferredItem<T extends Item> extends DeferredHolder<Item, T> implements ItemLike {

	public DeferredItem(ResourceKey<Item> key, @org.jetbrains.annotations.Nullable Supplier<? extends T> supplier) {
		super(key, supplier);
	}

	@Override
	public Item asItem() {
		return get();
	}

	public ItemStack getDefaultInstance() {
		return get().getDefaultInstance();
	}

	public ItemStack toStack() {
		return new ItemStack((ItemLike) this);
	}

	public ItemStack toStack(int count) {
		return new ItemStack((ItemLike) this, count);
	}
}
