package twilightforest.port.neoforged.neoforge.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import twilightforest.port.neoforged.bus.api.ICancellableEvent;
import twilightforest.port.neoforged.neoforge.event.entity.EntityEvent;

public class LivingFallEvent extends EntityEvent implements ICancellableEvent {
	private final LivingEntity entity;
	private float distance;
	private float damageMultiplier;
	private boolean canceled;

	public LivingFallEvent(LivingEntity entity, float distance, float damageMultiplier) {
		super(entity);
		this.entity = entity;
		this.distance = distance;
		this.damageMultiplier = damageMultiplier;
	}

	@Override
	public LivingEntity getEntity() {
		return entity;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public float getDamageMultiplier() {
		return damageMultiplier;
	}

	public void setDamageMultiplier(float damageMultiplier) {
		this.damageMultiplier = damageMultiplier;
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
