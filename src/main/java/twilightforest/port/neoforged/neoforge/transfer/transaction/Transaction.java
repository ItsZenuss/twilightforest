package twilightforest.port.neoforged.neoforge.transfer.transaction;

/**
 * Fabric port of NeoForge's Transaction, delegating to Fabric's transfer-API transactions (so changes roll back on
 * abort and other mods' storages take part).
 */
public class Transaction implements TransactionContext, AutoCloseable {
	private final net.fabricmc.fabric.api.transfer.v1.transaction.Transaction fabric;

	private Transaction(net.fabricmc.fabric.api.transfer.v1.transaction.Transaction fabric) {
		this.fabric = fabric;
	}

	/**
	 * Opens an outer transaction, or a nested one if a transaction is already open on this thread.
	 */
	public static Transaction openRoot() {
		if (net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.isOpen()) {
			return new Transaction(net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openNested(
				net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.getCurrentUnsafe()));
		}
		return new Transaction(net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter());
	}

	public static Transaction openNested(TransactionContext parent) {
		return new Transaction(parent.fabric().openNested());
	}

	public static Transaction open(@org.jetbrains.annotations.Nullable TransactionContext parent) {
		return parent == null ? openRoot() : openNested(parent);
	}

	@Override
	public net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext fabric() {
		return this.fabric;
	}

	public void commit() {
		this.fabric.commit();
	}

	public void abort() {
		this.fabric.abort();
	}

	@Override
	public void close() {
		this.fabric.close();
	}
}
