package twilightforest.port.neoforged.neoforge.transfer;

import twilightforest.port.neoforged.neoforge.transfer.transaction.TransactionContext;

public final class ResourceHandlerUtil {
	private ResourceHandlerUtil() {}

	public static <T> int insertStacking(ResourceHandler<T> handler, T resource, int amount, TransactionContext transaction) {
		return handler.insert(resource, amount, transaction);
	}
}
