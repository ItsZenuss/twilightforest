package twilightforest.port.neoforged.neoforge.event.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.port.neoforged.bus.api.Event;

public class BreakBlockEvent extends Event {
	private final Level level;
	private final BlockPos pos;
	private final BlockState state;
	private final Player player;

	public BreakBlockEvent(Level level, BlockPos pos, BlockState state, Player player) {
		this.level = level;
		this.pos = pos;
		this.state = state;
		this.player = player;
	}

	public Level getLevel() {
		return level;
	}

	public BlockPos getPos() {
		return pos;
	}

	public BlockState getState() {
		return state;
	}

	public Player getPlayer() {
		return player;
	}

	private boolean notifyClient;

	public void setNotifyClient(boolean notifyClient) {
		this.notifyClient = notifyClient;
	}

	public boolean isNotifyClient() {
		return notifyClient;
	}

	@Override
	public boolean isCancelable() {
		return true;
	}
}
