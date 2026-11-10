package twilightforest.port.neoforged.bus.api;

import java.util.function.Consumer;

/**
 * Fabric port of NeoForge's event bus. Listeners must name their event class explicitly: Java lambdas erase it,
 * so {@link #addListener(Consumer)} cannot recover it and throws instead of silently dropping the listener.
 */
public interface IEventBus {
	<T> void addListener(EventPriority priority, boolean receiveCanceled, Class<T> eventClass, Consumer<T> consumer);

	default <T> void addListener(Class<T> eventClass, Consumer<T> consumer) {
		this.addListener(EventPriority.NORMAL, false, eventClass, consumer);
	}

	default <T> void addListener(EventPriority priority, Class<T> eventClass, Consumer<T> consumer) {
		this.addListener(priority, false, eventClass, consumer);
	}

	default <T> void addListener(Consumer<T> consumer) {
		throw new UnsupportedOperationException("Fabric port: pass the event class explicitly to addListener");
	}

	default <T> void addListener(EventPriority priority, Consumer<T> consumer) {
		this.addListener(consumer);
	}

	<T> T post(T event);

	/**
	 * @return true if anything is listening for this event class or one of its supertypes, so callers can skip
	 * building expensive events.
	 */
	boolean hasListeners(Class<?> eventClass);
}
