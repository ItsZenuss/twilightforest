package twilightforest.port.neoforged.neoforge.common.world;

import net.minecraft.core.BlockPos;

public interface AuxiliaryLightManager {
	int getLightAt(BlockPos pos);
	void setLightAt(BlockPos pos, int light);
	default void removeLightAt(BlockPos pos) {}
}
