package twilightforest.port.neoforged.neoforge.common.util;

import java.util.*;

public class ConcatenatedListView<T> extends AbstractList<T> {
	private final List<? extends List<? extends T>> lists;

	@SafeVarargs
	public ConcatenatedListView(List<? extends T>... lists) {
		this.lists = Arrays.asList(lists);
	}

	@SafeVarargs
	public static <T> ConcatenatedListView<T> of(List<? extends T>... lists) {
		return new ConcatenatedListView<>(lists);
	}

	@Override
	public T get(int index) {
		int current = 0;
		for (List<? extends T> list : lists) {
			if (index < current + list.size()) {
				return list.get(index - current);
			}
			current += list.size();
		}
		throw new IndexOutOfBoundsException(index);
	}

	@Override
	public int size() {
		int size = 0;
		for (List<? extends T> list : lists) {
			size += list.size();
		}
		return size;
	}
}
