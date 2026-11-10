package twilightforest.port.neoforged.neoforge.event.entity.player;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import twilightforest.port.neoforged.bus.api.ICancellableEvent;

public class AttackEntityEvent extends PlayerEvent implements ICancellableEvent {
	private final Entity target;
	private boolean canceled;

	public AttackEntityEvent(Player player, Entity target) {
		super(player);
		this.target = target;
	}

	public Entity getTarget() {
		return target;
	}

	@Override
	public boolean isCanceled() {
		return canceled;
	}

	@Override
	public void setCanceled(boolean cancel) {
		this.canceled = cancel;
	}
}
