package twilightforest.port.neoforged.neoforge.event.entity;

import net.minecraft.world.entity.Entity;
import twilightforest.port.neoforged.bus.api.ICancellableEvent;

public class EntityTeleportEvent extends EntityEvent implements ICancellableEvent {
	private double targetX;
	private double targetY;
	private double targetZ;
	private boolean canceled;

	public EntityTeleportEvent(Entity entity, double targetX, double targetY, double targetZ) {
		super(entity);
		this.targetX = targetX;
		this.targetY = targetY;
		this.targetZ = targetZ;
	}

	public double getTargetX() {
		return targetX;
	}

	public void setTargetX(double targetX) {
		this.targetX = targetX;
	}

	public double getTargetY() {
		return targetY;
	}

	public void setTargetY(double targetY) {
		this.targetY = targetY;
	}

	public double getTargetZ() {
		return targetZ;
	}

	public void setTargetZ(double targetZ) {
		this.targetZ = targetZ;
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
