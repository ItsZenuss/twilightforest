package twilightforest.port.neoforged.neoforge.event.level;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import twilightforest.port.neoforged.bus.api.Event;
import twilightforest.port.neoforged.bus.api.ICancellableEvent;

import java.util.List;

public abstract class ExplosionEvent extends Event {
	private final Level level;
	private final Explosion explosion;

	public ExplosionEvent(Level level, Explosion explosion) {
		this.level = level;
		this.explosion = explosion;
	}

	public Level getLevel() {
		return level;
	}

	public Explosion getExplosion() {
		return explosion;
	}

	public static class Start extends ExplosionEvent implements ICancellableEvent {
		private boolean canceled;

		public Start(Level level, Explosion explosion) {
			super(level, explosion);
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

	public static class Detonate extends ExplosionEvent {
		private final List<Entity> entityList;

		public Detonate(Level level, Explosion explosion, List<Entity> entityList) {
			super(level, explosion);
			this.entityList = entityList;
		}

		public List<Entity> getAffectedEntities() {
			return entityList;
		}
	}
}
