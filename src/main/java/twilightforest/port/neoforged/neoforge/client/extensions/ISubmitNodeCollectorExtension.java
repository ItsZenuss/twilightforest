package twilightforest.port.neoforged.neoforge.client.extensions;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;

import java.util.List;

public interface ISubmitNodeCollectorExtension {
	default void submitMultiLayerBlockModel(PoseStack poseStack, List<BlockStateModelPart> parts, boolean translucent, int[] tints, int lightCoords, int overlay, int i) {
	}
}
