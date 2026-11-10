package twilightforest.port.neoforged.neoforge.transfer.item;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Objects;

public class ItemResource {
	public static final ItemResource EMPTY = new ItemResource(Items.AIR);

	private final Item item;
	private final DataComponentPatch components;

	public ItemResource(Item item) {
		this(item, DataComponentPatch.EMPTY);
	}

	public ItemResource(Item item, DataComponentPatch components) {
		this.item = item;
		this.components = components != null ? components : DataComponentPatch.EMPTY;
	}

	public static ItemResource of(ItemStack stack) {
		if (stack == null || stack.isEmpty()) return EMPTY;
		return new ItemResource(stack.getItem(), stack.getComponentsPatch());
	}

	public Item getItem() {
		return item;
	}

	public DataComponentPatch getComponentsPatch() {
		return components;
	}

	public boolean isEmpty() {
		return item == Items.AIR;
	}

	public ItemStack toStack() {
		return toStack(1);
	}

	public ItemStack toStack(int count) {
		if (isEmpty()) return ItemStack.EMPTY;
		ItemStack stack = new ItemStack(item, count);
		stack.applyComponents(components);
		return stack;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ItemResource that = (ItemResource) o;
		return item == that.item && Objects.equals(components, that.components);
	}

	@Override
	public int hashCode() {
		return Objects.hash(item, components);
	}
}
