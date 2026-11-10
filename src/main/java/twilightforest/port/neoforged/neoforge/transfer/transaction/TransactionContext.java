package twilightforest.port.neoforged.neoforge.transfer.transaction;

/**
 * Fabric port: a NeoForge transaction context backed by a Fabric transfer-API transaction.
 */
public interface TransactionContext {
	net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext fabric();

	static TransactionContext of(net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext fabric) {
		return () -> fabric;
	}
}
