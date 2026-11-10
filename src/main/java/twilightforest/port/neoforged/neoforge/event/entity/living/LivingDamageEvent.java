package twilightforest.port.neoforged.neoforge.event.entity.living;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.port.neoforged.neoforge.event.entity.EntityEvent;

public abstract class LivingDamageEvent extends EntityEvent {
	private final LivingEntity entity;
	private final DamageSource source;
	private final float originalDamage;
	private float newDamage;

	public LivingDamageEvent(LivingEntity entity, DamageSource source, float originalDamage) {
		super(entity);
		this.entity = entity;
		this.source = source;
		this.originalDamage = originalDamage;
		this.newDamage = originalDamage;
	}

	@Override
	public LivingEntity getEntity() {
		return entity;
	}

	public DamageSource getSource() {
		return source;
	}

	public float getOriginalDamage() {
		return originalDamage;
	}

	public float getNewDamage() {
		return newDamage;
	}

	public static class Pre extends LivingDamageEvent {
		public Pre(LivingEntity entity, DamageSource source, float originalDamage) {
			super(entity, source, originalDamage);
		}
	}

	public static class Post extends LivingDamageEvent {
		public Post(LivingEntity entity, DamageSource source, float originalDamage) {
			super(entity, source, originalDamage);
		}
	}
}
