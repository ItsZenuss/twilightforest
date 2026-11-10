package twilightforest.port.neoforged.neoforge.event.entity.living;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.port.neoforged.bus.api.ICancellableEvent;
import twilightforest.port.neoforged.neoforge.event.entity.EntityEvent;

public class LivingDeathEvent extends EntityEvent implements ICancellableEvent {
	private final LivingEntity entity;
	private final DamageSource source;
	private boolean canceled;

	public LivingDeathEvent(LivingEntity entity, DamageSource source) {
		super(entity);
		this.entity = entity;
		this.source = source;
	}

	@Override
	public LivingEntity getEntity() {
		return entity;
	}

	public DamageSource getSource() {
		return source;
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
