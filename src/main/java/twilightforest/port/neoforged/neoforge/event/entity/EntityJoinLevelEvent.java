package twilightforest.port.neoforged.neoforge.event.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import twilightforest.port.neoforged.bus.api.ICancellableEvent;

public class EntityJoinLevelEvent extends EntityEvent implements ICancellableEvent {
	private final Level level;
	private final boolean loadedFromDisk;
	private boolean canceled;

	public EntityJoinLevelEvent(Entity entity, Level level) {
		this(entity, level, false);
	}

	public EntityJoinLevelEvent(Entity entity, Level level, boolean loadedFromDisk) {
		super(entity);
		this.level = level;
		this.loadedFromDisk = loadedFromDisk;
	}

	public Level getLevel() {
		return level;
	}

	public boolean loadedFromDisk() {
		return loadedFromDisk;
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
