package twilightforest.port.neoforged.neoforge.event.entity.player;

import net.minecraft.world.entity.player.Player;

public class PlayerSpawnPhantomsEvent extends PlayerEvent {
	public enum Result {
		ALLOW,
		DENY,
		DEFAULT
	}

	private Result result = Result.DEFAULT;

	public PlayerSpawnPhantomsEvent(Player player) {
		super(player);
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}
}
