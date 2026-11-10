package twilightforest.port.neoforged.neoforge.client.extensions;

import net.minecraft.core.Holder;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface IMapRenderStateExtension {
	Map<Object, Map<ContextKey<?>, Object>> DATA = new ConcurrentHashMap<>();
	Map<Object, Holder<MapDecorationType>> TYPES = new ConcurrentHashMap<>();

	default <T> void setRenderData(ContextKey<T> key, T value) {
		DATA.computeIfAbsent(this, k -> new ConcurrentHashMap<>()).put(key, value);
	}

	@SuppressWarnings("unchecked")
	default <T> T getRenderData(ContextKey<T> key) {
		Map<ContextKey<?>, Object> map = DATA.get(this);
		return map != null ? (T) map.get(key) : null;
	}

	default Holder<MapDecorationType> getType() {
		return TYPES.get(this);
	}

	default void setType(Holder<MapDecorationType> type) {
		TYPES.put(this, type);
	}
}
