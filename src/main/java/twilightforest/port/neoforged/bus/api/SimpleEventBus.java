package twilightforest.port.neoforged.bus.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Minimal NeoForge-compatible event bus: dispatches to listeners of the event's class and its supertypes, in
 * priority order, honouring cancellation.
 */
public class SimpleEventBus implements IEventBus {
	private static final Logger LOGGER = LoggerFactory.getLogger("twilightforest-eventbus");

	private record Listener(EventPriority priority, boolean receiveCanceled, Class<?> eventClass, Consumer<Object> consumer, long order) {}

	private final List<Listener> listeners = new CopyOnWriteArrayList<>();
	private final Map<Class<?>, List<Listener>> dispatchCache = new ConcurrentHashMap<>();
	private long counter;

	@Override
	@SuppressWarnings("unchecked")
	public synchronized <T> void addListener(EventPriority priority, boolean receiveCanceled, Class<T> eventClass, Consumer<T> consumer) {
		this.listeners.add(new Listener(priority, receiveCanceled, eventClass, (Consumer<Object>) consumer, this.counter++));
		this.dispatchCache.clear();
	}

	private List<Listener> listenersFor(Class<?> type) {
		return this.dispatchCache.computeIfAbsent(type, t -> {
			List<Listener> list = new ArrayList<>();
			for (Listener l : this.listeners) {
				if (l.eventClass().isAssignableFrom(t)) list.add(l);
			}
			list.sort(Comparator.comparing(Listener::priority).thenComparingLong(Listener::order));
			return List.copyOf(list);
		});
	}

	@Override
	public boolean hasListeners(Class<?> eventClass) {
		return !this.listenersFor(eventClass).isEmpty();
	}

	@Override
	public <T> T post(T event) {
		if (event == null) return null;
		for (Listener l : this.listenersFor(event.getClass())) {
			if (!l.receiveCanceled() && isCanceled(event)) continue;
			try {
				l.consumer().accept(event);
			} catch (Exception e) {
				LOGGER.error("Exception handling {}", event.getClass().getName(), e);
			}
		}
		return event;
	}

	private static boolean isCanceled(Object event) {
		if (event instanceof ICancellableEvent c) return c.isCanceled();
		if (event instanceof Event e) return e.isCanceled();
		return false;
	}
}
