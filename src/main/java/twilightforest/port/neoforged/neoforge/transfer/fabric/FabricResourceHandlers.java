package twilightforest.port.neoforged.neoforge.transfer.fabric;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import twilightforest.port.neoforged.neoforge.transfer.ResourceHandler;
import twilightforest.port.neoforged.neoforge.transfer.fluid.FluidResource;
import twilightforest.port.neoforged.neoforge.transfer.item.ItemResource;
import twilightforest.port.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * Fabric port: adapters between NeoForge-style {@link ResourceHandler}s and Fabric transfer-API {@link Storage}s.
 */
public final class FabricResourceHandlers {
	private FabricResourceHandlers() {}

	public static ItemVariant toVariant(ItemResource resource) {
		return resource.isEmpty() ? ItemVariant.blank() : ItemVariant.of(resource.getItem(), resource.getComponentsPatch());
	}

	public static ItemResource fromVariant(ItemVariant variant) {
		return variant.isBlank() ? ItemResource.EMPTY : new ItemResource(variant.getItem(), variant.getComponentsPatch());
	}

	public static FluidVariant toVariant(FluidResource resource) {
		return resource.isEmpty() ? FluidVariant.blank() : FluidVariant.of(resource.getFluid(), resource.getComponentsPatch());
	}

	public static FluidResource fromVariant(FluidVariant variant) {
		return variant.isBlank() ? FluidResource.EMPTY : new FluidResource(variant.getFluid(), variant.getComponentsPatch());
	}

	@Nullable
	public static ResourceHandler<ItemResource> items(@Nullable Storage<ItemVariant> storage) {
		return storage == null ? null : new StorageHandler<>(storage, FabricResourceHandlers::toVariant, FabricResourceHandlers::fromVariant, ItemResource.EMPTY);
	}

	@Nullable
	public static ResourceHandler<FluidResource> fluids(@Nullable Storage<FluidVariant> storage) {
		return storage == null ? null : new StorageHandler<>(storage, FabricResourceHandlers::toVariant, FabricResourceHandlers::fromVariant, FluidResource.EMPTY);
	}

	public static Storage<ItemVariant> itemStorage(ResourceHandler<ItemResource> handler) {
		return new HandlerStorage<>(handler, FabricResourceHandlers::toVariant, FabricResourceHandlers::fromVariant);
	}

	/**
	 * NeoForge handler view of a Fabric storage. Slotted storages map 1:1 to indices; others expose a snapshot of
	 * their views (inserts then go through the storage as a whole).
	 */
	private static final class StorageHandler<R, V> implements ResourceHandler<R> {
		private final Storage<V> storage;
		private final Function<R, V> toVariant;
		private final Function<V, R> fromVariant;
		private final R empty;

		StorageHandler(Storage<V> storage, Function<R, V> toVariant, Function<V, R> fromVariant, R empty) {
			this.storage = storage;
			this.toVariant = toVariant;
			this.fromVariant = fromVariant;
			this.empty = empty;
		}

		private List<StorageView<V>> views() {
			List<StorageView<V>> list = new ArrayList<>();
			for (Iterator<StorageView<V>> it = this.storage.iterator(); it.hasNext(); ) list.add(it.next());
			return list;
		}

		@Nullable
		private StorageView<V> view(int index) {
			if (this.storage instanceof SlottedStorage<V> slotted) {
				return index >= 0 && index < slotted.getSlotCount() ? slotted.getSlot(index) : null;
			}
			List<StorageView<V>> views = this.views();
			return index >= 0 && index < views.size() ? views.get(index) : null;
		}

		@Override
		public int size() {
			return this.storage instanceof SlottedStorage<V> slotted ? slotted.getSlotCount() : this.views().size();
		}

		@Override
		public R getResource(int index) {
			StorageView<V> view = this.view(index);
			return view == null || view.isResourceBlank() ? this.empty : this.fromVariant.apply(view.getResource());
		}

		@Override
		public long getAmountAsLong(int index) {
			StorageView<V> view = this.view(index);
			return view == null ? 0 : view.getAmount();
		}

		@Override
		public int insert(int index, R resource, int amount, TransactionContext transaction) {
			if (amount <= 0) return 0;
			if (this.storage instanceof SlottedStorage<V> slotted) {
				if (index < 0 || index >= slotted.getSlotCount()) return 0;
				return (int) slotted.getSlot(index).insert(this.toVariant.apply(resource), amount, transaction.fabric());
			}
			return (int) this.storage.insert(this.toVariant.apply(resource), amount, transaction.fabric());
		}

		@Override
		public int extract(int index, R resource, int amount, TransactionContext transaction) {
			StorageView<V> view = this.view(index);
			if (view == null || amount <= 0) return 0;
			V variant = this.toVariant.apply(resource);
			return (int) view.extract(variant, amount, transaction.fabric());
		}

		@Override
		public int insert(R resource, int amount, TransactionContext transaction) {
			return amount <= 0 ? 0 : (int) this.storage.insert(this.toVariant.apply(resource), amount, transaction.fabric());
		}

		@Override
		public int extract(R resource, int amount, TransactionContext transaction) {
			return amount <= 0 ? 0 : (int) this.storage.extract(this.toVariant.apply(resource), amount, transaction.fabric());
		}
	}

	/**
	 * Fabric storage view of a NeoForge handler, so hoppers, pipes and other mods can use Twilight Forest inventories.
	 */
	private static final class HandlerStorage<R, V> implements SlottedStorage<V> {
		private final ResourceHandler<R> handler;
		private final Function<R, V> toVariant;
		private final Function<V, R> fromVariant;

		HandlerStorage(ResourceHandler<R> handler, Function<R, V> toVariant, Function<V, R> fromVariant) {
			this.handler = handler;
			this.toVariant = toVariant;
			this.fromVariant = fromVariant;
		}

		@Override
		public long insert(V resource, long maxAmount, net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext transaction) {
			return this.handler.insert(this.fromVariant.apply(resource), (int) Math.min(Integer.MAX_VALUE, maxAmount), TransactionContext.of(transaction));
		}

		@Override
		public long extract(V resource, long maxAmount, net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext transaction) {
			return this.handler.extract(this.fromVariant.apply(resource), (int) Math.min(Integer.MAX_VALUE, maxAmount), TransactionContext.of(transaction));
		}

		@Override
		public int getSlotCount() {
			return this.handler.size();
		}

		@Override
		public SingleSlotStorage<V> getSlot(int index) {
			return new SingleSlotStorage<>() {
				@Override
				public long insert(V resource, long maxAmount, net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext transaction) {
					return HandlerStorage.this.handler.insert(index, HandlerStorage.this.fromVariant.apply(resource), (int) Math.min(Integer.MAX_VALUE, maxAmount), TransactionContext.of(transaction));
				}

				@Override
				public long extract(V resource, long maxAmount, net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext transaction) {
					return HandlerStorage.this.handler.extract(index, HandlerStorage.this.fromVariant.apply(resource), (int) Math.min(Integer.MAX_VALUE, maxAmount), TransactionContext.of(transaction));
				}

				@Override
				public boolean isResourceBlank() {
					return HandlerStorage.this.handler.getAmountAsLong(index) <= 0;
				}

				@Override
				public V getResource() {
					return HandlerStorage.this.toVariant.apply(HandlerStorage.this.handler.getResource(index));
				}

				@Override
				public long getAmount() {
					return HandlerStorage.this.handler.getAmountAsLong(index);
				}

				@Override
				public long getCapacity() {
					return 64;
				}
			};
		}

		@Override
		public Iterator<StorageView<V>> iterator() {
			List<StorageView<V>> views = new ArrayList<>(this.getSlotCount());
			for (int i = 0; i < this.getSlotCount(); i++) views.add(this.getSlot(i));
			return views.iterator();
		}
	}
}
