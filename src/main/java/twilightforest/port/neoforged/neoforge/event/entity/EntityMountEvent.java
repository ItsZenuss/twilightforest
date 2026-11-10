package twilightforest.port.neoforged.neoforge.event.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import twilightforest.port.neoforged.bus.api.ICancellableEvent;

public class EntityMountEvent extends EntityEvent implements ICancellableEvent {
	private final Entity entityMounting;
	private final Entity entityBeingMounted;
	private final Level level;
	private final boolean isMounting;
	private boolean canceled;

	public EntityMountEvent(Entity entityMounting, Entity entityBeingMounted, Level level, boolean isMounting) {
		super(entityMounting);
		this.entityMounting = entityMounting;
		this.entityBeingMounted = entityBeingMounted;
		this.level = level;
		this.isMounting = isMounting;
	}

	public Entity getEntityMounting() {
		return entityMounting;
	}

	public Entity getEntityBeingMounted() {
		return entityBeingMounted;
	}

	public Level getLevel() {
		return level;
	}

	public boolean isMounting() {
		return isMounting;
	}

	public boolean isDismounting() {
		return !isMounting;
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
