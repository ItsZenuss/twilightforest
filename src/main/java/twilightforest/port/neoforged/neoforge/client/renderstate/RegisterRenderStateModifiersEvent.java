package twilightforest.port.neoforged.neoforge.client.renderstate;

import com.google.common.reflect.TypeToken;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import twilightforest.port.neoforged.bus.api.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Fabric port: render-state modifiers are applied by mixins after vanilla extracts entity and map render states
 * ({@code twilightforest.mixin.client.EntityRendererStateMixin} and {@code MapRendererMixin}).
 */
public class RegisterRenderStateModifiersEvent extends Event {
	public record EntityModifier(Class<?> rendererClass, BiConsumer<Object, Object> consumer) {}

	private static final List<EntityModifier> ENTITY_MODIFIERS = new ArrayList<>();
	private static final List<BiConsumer<MapItemSavedData, MapRenderState>> MAP_MODIFIERS = new ArrayList<>();
	private static final Map<ResourceKey<MapDecorationType>, List<MapDecorationRenderStateModifier>> DECORATION_MODIFIERS = new HashMap<>();

	@SuppressWarnings("unchecked")
	public void registerMapModifier(Object modifier) {
		MAP_MODIFIERS.add((BiConsumer<MapItemSavedData, MapRenderState>) modifier);
	}

	public void registerMapDecorationModifier(ResourceKey<MapDecorationType> key, Object modifier) {
		DECORATION_MODIFIERS.computeIfAbsent(key, k -> new ArrayList<>()).add((MapDecorationRenderStateModifier) modifier);
	}

	@SuppressWarnings("unchecked")
	public <E, S> void registerEntityModifier(TypeToken<?> token, BiConsumer<E, S> consumer) {
		ENTITY_MODIFIERS.add(new EntityModifier(token.getRawType(), (BiConsumer<Object, Object>) consumer));
	}

	public static List<EntityModifier> getEntityModifiers() {
		return ENTITY_MODIFIERS;
	}

	public static void applyMapModifiers(MapItemSavedData data, MapRenderState state) {
		for (BiConsumer<MapItemSavedData, MapRenderState> modifier : MAP_MODIFIERS) modifier.accept(data, state);
		for (MapRenderState.MapDecorationRenderState decoration : state.decorations) {
			if (decoration.getType() == null) continue;
			decoration.getType().unwrapKey().map(DECORATION_MODIFIERS::get).ifPresent(list -> {
				for (MapDecorationRenderStateModifier modifier : list) modifier.accept(data, state, decoration);
			});
		}
	}
}
