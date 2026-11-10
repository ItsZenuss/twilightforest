package twilightforest.port.neoforged.neoforge.transfer;

import twilightforest.port.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.List;

public class CombinedResourceHandler<T> implements ResourceHandler<T> {
	private final List<ResourceHandler<T>> handlers;

	@SafeVarargs
	public CombinedResourceHandler(ResourceHandler<T>... handlers) {
		this.handlers = List.of(handlers);
	}

	public CombinedResourceHandler(List<ResourceHandler<T>> handlers) {
		this.handlers = handlers;
	}

	@Override
	public int size() {
		return handlers.stream().mapToInt(ResourceHandler::size).sum();
	}

	@Override
	public T getResource(int index) {
		for (ResourceHandler<T> handler : handlers) {
			if (index < handler.size()) return handler.getResource(index);
			index -= handler.size();
		}
		return null;
	}

	@Override
	public long getAmountAsLong(int index) {
		for (ResourceHandler<T> handler : handlers) {
			if (index < handler.size()) return handler.getAmountAsLong(index);
			index -= handler.size();
		}
		return 0;
	}

	@Override
	public int insert(int index, T resource, int amount, TransactionContext transaction) {
		for (ResourceHandler<T> handler : handlers) {
			if (index < handler.size()) return handler.insert(index, resource, amount, transaction);
			index -= handler.size();
		}
		return 0;
	}

	@Override
	public int extract(int index, T resource, int amount, TransactionContext transaction) {
		for (ResourceHandler<T> handler : handlers) {
			if (index < handler.size()) return handler.extract(index, resource, amount, transaction);
			index -= handler.size();
		}
		return 0;
	}
}
