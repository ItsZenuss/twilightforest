package twilightforest.port.neoforged.neoforge.event.entity.living;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.port.neoforged.bus.api.ICancellableEvent;
import twilightforest.port.neoforged.neoforge.common.damagesource.DamageContainer;
import twilightforest.port.neoforged.neoforge.event.entity.EntityEvent;

public class LivingIncomingDamageEvent extends EntityEvent implements ICancellableEvent {
	private final LivingEntity entity;
	private final DamageContainer container;
	private boolean canceled;

	public LivingIncomingDamageEvent(LivingEntity entity, DamageContainer container) {
		super(entity);
		this.entity = entity;
		this.container = container;
	}

	@Override
	public LivingEntity getEntity() {
		return entity;
	}

	public DamageContainer getContainer() {
		return container;
	}

	public DamageSource getSource() {
		return container.getSource();
	}

	public float getAmount() {
		return container.getNewDamage();
	}

	public void setAmount(float amount) {
		container.setNewDamage(amount);
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
