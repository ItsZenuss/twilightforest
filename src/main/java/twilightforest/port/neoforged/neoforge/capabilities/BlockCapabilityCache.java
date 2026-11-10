package twilightforest.port.neoforged.neoforge.capabilities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

public class BlockCapabilityCache<R, C> {
	private final BlockCapability<R, C> capability;
	private final ServerLevel level;
	private final BlockPos pos;
	private final C context;

	protected BlockCapabilityCache(BlockCapability<R, C> capability, ServerLevel level, BlockPos pos, C context) {
		this.capability = capability;
		this.level = level;
		this.pos = pos;
		this.context = context;
	}

	public static <R, C> BlockCapabilityCache<R, C> create(BlockCapability<R, C> capability, ServerLevel level, BlockPos pos, C context) {
		return new BlockCapabilityCache<>(capability, level, pos, context);
	}

	@Nullable
	public R getCapability() {
		return capability.getCapability(level, pos, context);
	}
}
