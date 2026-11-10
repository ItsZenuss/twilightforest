package twilightforest.mixin.event;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.core.Holder;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.level.LevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

/** Fabric port: fires LevelEvent.PotentialSpawns so listeners can edit the spawn list at a position. */
@Mixin(NaturalSpawner.class)
public abstract class NaturalSpawnerEventMixin {
	@ModifyReturnValue(method = "mobsAt", at = @At("RETURN"))
	private static WeightedList<MobSpawnSettings.SpawnerData> tf$potentialSpawns(WeightedList<MobSpawnSettings.SpawnerData> original, ServerLevel level, StructureManager structures,
																			   ChunkGenerator generator, MobCategory category, BlockPos pos, Holder<Biome> biome) {
		List<Weighted<MobSpawnSettings.SpawnerData>> list = new ArrayList<>(original.unwrap());
		LevelEvent.PotentialSpawns event = NeoForge.EVENT_BUS.post(new LevelEvent.PotentialSpawns(level, category, pos, list));
		return event.getSpawnerDataList().equals(original.unwrap()) ? original : WeightedList.of(event.getSpawnerDataList());
	}
}
