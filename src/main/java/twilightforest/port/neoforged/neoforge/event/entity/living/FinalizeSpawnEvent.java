package twilightforest.port.neoforged.neoforge.event.entity.living;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ServerLevelAccessor;
import twilightforest.port.neoforged.neoforge.event.entity.EntityEvent;

public class FinalizeSpawnEvent extends EntityEvent {
	private final Mob mob;
	private final ServerLevelAccessor level;
	private final DifficultyInstance difficulty;

	public FinalizeSpawnEvent(Mob mob, ServerLevelAccessor level, DifficultyInstance difficulty) {
		super(mob);
		this.mob = mob;
		this.level = level;
		this.difficulty = difficulty;
	}

	@Override
	public Mob getEntity() {
		return mob;
	}

	public ServerLevelAccessor getLevel() {
		return level;
	}

	public DifficultyInstance getDifficulty() {
		return difficulty;
	}
}
