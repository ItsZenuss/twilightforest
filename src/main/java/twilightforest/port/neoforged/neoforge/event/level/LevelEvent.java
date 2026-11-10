package twilightforest.port.neoforged.neoforge.event.level;

import net.minecraft.core.BlockPos;
import net.minecraft.util.random.Weighted;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.MobSpawnSettings;
import twilightforest.port.neoforged.bus.api.Event;

import java.util.ArrayList;
import java.util.List;

public abstract class LevelEvent extends Event {
	private final LevelAccessor level;

	public LevelEvent(LevelAccessor level) {
		this.level = level;
	}

	public LevelAccessor getLevel() {
		return level;
	}

	public static class PotentialSpawns extends LevelEvent {
		private final MobCategory mobCategory;
		private final BlockPos pos;
		private final List<Weighted<MobSpawnSettings.SpawnerData>> spawnerDataList;

		public PotentialSpawns(LevelAccessor level, MobCategory mobCategory, BlockPos pos, List<Weighted<MobSpawnSettings.SpawnerData>> spawnerDataList) {
			super(level);
			this.mobCategory = mobCategory;
			this.pos = pos;
			this.spawnerDataList = spawnerDataList != null ? spawnerDataList : new ArrayList<>();
		}

		public MobCategory getMobCategory() {
			return mobCategory;
		}

		public BlockPos getPos() {
			return pos;
		}

		public List<Weighted<MobSpawnSettings.SpawnerData>> getSpawnerDataList() {
			return spawnerDataList;
		}

		public void addSpawnerData(Weighted<MobSpawnSettings.SpawnerData> data) {
			spawnerDataList.add(data);
		}

		public boolean removeSpawnerData(Weighted<MobSpawnSettings.SpawnerData> data) {
			return spawnerDataList.remove(data);
		}
	}
}
