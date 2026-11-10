package twilightforest.port.neoforged.neoforge.client.extensions;

import net.minecraft.util.context.ContextKey;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface IEntityRenderStateExtension {
	Map<ContextKey<?>, Object> tw_renderData = new ConcurrentHashMap<>();

	default <T> void setRenderData(ContextKey<T> key, T value) {
		if (value == null) {
			tw_renderData.remove(key);
		} else {
			tw_renderData.put(key, value);
		}
	}

	@SuppressWarnings("unchecked")
	default <T> T getRenderData(ContextKey<T> key) {
		return (T) tw_renderData.get(key);
	}
}
