package twilightforest.port.neoforged.neoforge.entity;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public abstract class PartEntity<T extends Entity> extends Entity {
	private final T parent;

	public PartEntity(T parent) {
		super(parent.getType(), parent.level());
		this.parent = parent;
	}

	public T getParent() {
		return parent;
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public boolean hurtServer(net.minecraft.server.level.ServerLevel level, DamageSource source, float amount) {
		return isInvulnerableToBase(source) ? false : parent.hurtServer(level, source, amount);
	}

	@Override
	public boolean is(Entity entity) {
		return this == entity || this.parent == entity;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {}
}
