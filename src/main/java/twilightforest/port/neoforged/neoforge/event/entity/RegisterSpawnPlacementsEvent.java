package twilightforest.port.neoforged.neoforge.event.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

public class RegisterSpawnPlacementsEvent {
	public enum Operation {
		REPLACE,
		AND,
		OR
	}

	public <T extends Mob> void register(
		EntityType<T> entityType,
		SpawnPlacementType placementType,
		Heightmap.Types heightmapType,
		SpawnPlacements.SpawnPredicate<T> predicate,
		Operation operation
	) {
		try {
			SpawnPlacements.register(entityType, placementType, heightmapType, predicate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
