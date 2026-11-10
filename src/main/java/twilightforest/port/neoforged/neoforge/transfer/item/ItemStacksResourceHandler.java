package twilightforest.port.neoforged.neoforge.transfer.item;

import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import twilightforest.port.neoforged.neoforge.transfer.ResourceHandler;
import twilightforest.port.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Fabric port of NeoForge's ItemStacksResourceHandler. Participates in Fabric transactions (changes roll back on
 * abort), calls {@link #onContentsChanged} once the outermost transaction commits, and saves its contents.
 */
public class ItemStacksResourceHandler implements ResourceHandler<ItemResource> {
	public static final String VALUE_IO_KEY = "handler";
	protected NonNullList<ItemStack> stacks;
	private final Map<Integer, ItemStack> pendingChanges = new LinkedHashMap<>();

	private final SnapshotParticipant<List<ItemStack>> participant = new SnapshotParticipant<>() {
		@Override
		protected List<ItemStack> createSnapshot() {
			List<ItemStack> copy = new ArrayList<>(ItemStacksResourceHandler.this.stacks.size());
			for (ItemStack stack : ItemStacksResourceHandler.this.stacks) copy.add(stack.copy());
			return copy;
		}

		@Override
		protected void readSnapshot(List<ItemStack> snapshot) {
			for (int i = 0; i < snapshot.size(); i++) ItemStacksResourceHandler.this.stacks.set(i, snapshot.get(i));
		}

		@Override
		public void afterOuterClose(net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext.Result result) {
			super.afterOuterClose(result);
			if (result.wasCommitted()) {
				Map<Integer, ItemStack> changes = new LinkedHashMap<>(ItemStacksResourceHandler.this.pendingChanges);
				ItemStacksResourceHandler.this.pendingChanges.clear();
				changes.forEach(ItemStacksResourceHandler.this::onContentsChanged);
			} else {
				ItemStacksResourceHandler.this.pendingChanges.clear();
			}
		}
	};

	public ItemStacksResourceHandler(int size) {
		this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
	}

	public ItemStacksResourceHandler(NonNullList<ItemStack> stacks) {
		this.stacks = stacks;
	}

	@Override
	public int size() {
		return stacks.size();
	}

	@Override
	public ItemResource getResource(int index) {
		if (index < 0 || index >= stacks.size()) return ItemResource.EMPTY;
		return ItemResource.of(stacks.get(index));
	}

	@Override
	public long getAmountAsLong(int index) {
		if (index < 0 || index >= stacks.size()) return 0;
		return stacks.get(index).getCount();
	}

	public boolean isValid(int index, ItemResource resource) {
		return true;
	}

	protected int getCapacity(int index, ItemResource resource) {
		return resource.toStack().getMaxStackSize();
	}

	private void beforeChange(int index, TransactionContext transaction) {
		this.participant.updateSnapshots(transaction.fabric());
		this.pendingChanges.putIfAbsent(index, this.stacks.get(index).copy());
	}

	@Override
	public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
		if (index < 0 || index >= stacks.size() || resource.isEmpty() || amount <= 0 || !isValid(index, resource)) return 0;
		ItemStack current = stacks.get(index);
		int capacity = getCapacity(index, resource);
		if (current.isEmpty()) {
			int inserted = Math.min(amount, capacity);
			if (inserted <= 0) return 0;
			beforeChange(index, transaction);
			stacks.set(index, resource.toStack(inserted));
			return inserted;
		} else if (ItemStack.isSameItemSameComponents(current, resource.toStack())) {
			int space = Math.min(capacity, current.getMaxStackSize()) - current.getCount();
			if (space <= 0) return 0;
			int inserted = Math.min(amount, space);
			beforeChange(index, transaction);
			stacks.get(index).grow(inserted);
			return inserted;
		}
		return 0;
	}

	@Override
	public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
		if (index < 0 || index >= stacks.size() || amount <= 0) return 0;
		ItemStack current = stacks.get(index);
		if (current.isEmpty()) return 0;
		if (!resource.isEmpty() && !ItemStack.isSameItemSameComponents(current, resource.toStack())) return 0;
		int extracted = Math.min(amount, current.getCount());
		beforeChange(index, transaction);
		ItemStack updated = current.copy();
		updated.shrink(extracted);
		stacks.set(index, updated.isEmpty() ? ItemStack.EMPTY : updated);
		return extracted;
	}

	protected void onContentsChanged(int index, ItemStack previousContents) {}

	public void serialize(ValueOutput output) {
		ValueOutput.TypedOutputList<ItemStack> list = output.list(VALUE_IO_KEY, ItemStack.OPTIONAL_CODEC);
		for (ItemStack stack : this.stacks) list.add(stack);
	}

	public void deserialize(ValueInput input) {
		input.list(VALUE_IO_KEY, ItemStack.OPTIONAL_CODEC).ifPresent(list -> {
			int i = 0;
			for (ItemStack stack : list) {
				if (i >= this.stacks.size()) break;
				this.stacks.set(i++, stack);
			}
			for (; i < this.stacks.size(); i++) this.stacks.set(i, ItemStack.EMPTY);
		});
	}
}
