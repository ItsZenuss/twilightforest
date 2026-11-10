package twilightforest.port.neoforged.neoforge.transfer;

import twilightforest.port.neoforged.neoforge.transfer.transaction.TransactionContext;

public interface ResourceHandler<T> {
	int size();
	T getResource(int index);
	long getAmountAsLong(int index);

	default int getAmountAsInt(int index) {
		return (int) Math.min(Integer.MAX_VALUE, getAmountAsLong(index));
	}

	int insert(int index, T resource, int amount, TransactionContext transaction);
	int extract(int index, T resource, int amount, TransactionContext transaction);

	default int insert(T resource, int amount, TransactionContext transaction) {
		int remaining = amount;
		for (int i = 0; i < size(); i++) {
			int inserted = insert(i, resource, remaining, transaction);
			remaining -= inserted;
			if (remaining <= 0) break;
		}
		return amount - remaining;
	}

	default int extract(T resource, int amount, TransactionContext transaction) {
		int remaining = amount;
		for (int i = 0; i < size(); i++) {
			int extracted = extract(i, resource, remaining, transaction);
			remaining -= extracted;
			if (remaining <= 0) break;
		}
		return amount - remaining;
	}
}
