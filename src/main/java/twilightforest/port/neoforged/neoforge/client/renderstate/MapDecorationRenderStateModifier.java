package twilightforest.port.neoforged.neoforge.client.renderstate;

import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

@FunctionalInterface
public interface MapDecorationRenderStateModifier {
	void accept(MapItemSavedData mapData, MapRenderState mapRenderState, MapRenderState.MapDecorationRenderState decoration);
}
