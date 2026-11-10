package twilightforest.port.neoforged.neoforge.common.damagesource;

import net.minecraft.world.damagesource.DamageSource;

public class DamageContainer {
	private final DamageSource source;
	private final float originalDamage;
	private float newDamage;

	public DamageContainer(DamageSource source, float originalDamage) {
		this.source = source;
		this.originalDamage = originalDamage;
		this.newDamage = originalDamage;
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

	public void setNewDamage(float damage) {
		this.newDamage = damage;
	}
}
