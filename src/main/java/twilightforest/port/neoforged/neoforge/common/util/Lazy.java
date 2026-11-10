package twilightforest.port.neoforged.neoforge.common.util;

import java.util.function.Supplier;

public interface Lazy<T> extends Supplier<T> {
	static <T> Lazy<T> of(Supplier<T> supplier) {
		return new Lazy<>() {
			private T value;
			private boolean initialized = false;

			@Override
			public synchronized T get() {
				if (!initialized) {
					value = supplier.get();
					initialized = true;
				}
				return value;
			}

			@Override
			public synchronized void invalidate() {
				initialized = false;
				value = null;
			}
		};
	}

	default void invalidate() {
	}
}
