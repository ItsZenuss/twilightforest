package twilightforest.port.neoforged.neoforge.event.entity.living;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.port.neoforged.bus.api.Event;
import twilightforest.port.neoforged.neoforge.event.entity.EntityEvent;

public abstract class MobEffectEvent extends EntityEvent {
	private final LivingEntity entity;
	private final MobEffectInstance effectInstance;

	public MobEffectEvent(LivingEntity entity, MobEffectInstance effectInstance) {
		super(entity);
		this.entity = entity;
		this.effectInstance = effectInstance;
	}

	@Override
	public LivingEntity getEntity() {
		return entity;
	}

	public MobEffectInstance getEffectInstance() {
		return effectInstance;
	}

	public static class Applicable extends MobEffectEvent {
		public enum Result {
			APPLY,
			DO_NOT_APPLY
		}

		private Result result = Result.APPLY;

		public Applicable(LivingEntity entity, MobEffectInstance effectInstance) {
			super(entity, effectInstance);
		}

		public boolean getApplicationResult() {
			return result != Result.DO_NOT_APPLY;
		}

		public void setResult(Result result) {
			this.result = result;
		}

		public Result getResult() {
			return result;
		}
	}
}
