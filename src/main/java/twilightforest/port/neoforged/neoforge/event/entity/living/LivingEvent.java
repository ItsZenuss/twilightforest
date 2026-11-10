package twilightforest.port.neoforged.neoforge.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import twilightforest.port.neoforged.neoforge.event.entity.EntityEvent;

public class LivingEvent extends EntityEvent {
	private final LivingEntity entity;

	public LivingEvent(LivingEntity entity) {
		super(entity);
		this.entity = entity;
	}

	@Override
	public LivingEntity getEntity() {
		return entity;
	}

	public static class LivingJumpEvent extends LivingEvent {
		public LivingJumpEvent(LivingEntity entity) {
			super(entity);
		}
	}
}
