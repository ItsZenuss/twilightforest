package twilightforest.port.neoforged.neoforge.client.gui.map;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;

public interface IMapDecorationRenderer {
	boolean render(MapRenderState.MapDecorationRenderState decoration, PoseStack stack, SubmitNodeCollector submitNodeCollector, MapRenderState mapRenderState, TextureAtlas decorationSprites, boolean inItemFrame, int light, int index);
}
