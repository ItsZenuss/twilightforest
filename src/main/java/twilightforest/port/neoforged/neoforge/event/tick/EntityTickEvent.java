package twilightforest.port.neoforged.neoforge.event.tick;

import net.minecraft.world.entity.Entity;
import twilightforest.port.neoforged.neoforge.event.entity.EntityEvent;

public abstract class EntityTickEvent extends EntityEvent {
	public EntityTickEvent(Entity entity) {
		super(entity);
	}

	public static class Pre extends EntityTickEvent {
		public Pre(Entity entity) {
			super(entity);
		}
	}

	public static class Post extends EntityTickEvent {
		public Post(Entity entity) {
			super(entity);
		}
	}
}
