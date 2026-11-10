package twilightforest.port.neoforged.neoforge.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

import java.util.function.Consumer;

public final class EventHooks {
	private EventHooks() {}

	public static boolean canEntityGrief(Level level, Entity entity) {
		if (level instanceof ServerLevel serverLevel) {
			return serverLevel.getGameRules().get(GameRules.MOB_GRIEFING);
		}
		return false;
	}

	public static SpawnGroupData finalizeMobSpawn(Mob mob, ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason reason, SpawnGroupData data) {
		return mob.finalizeSpawn(level, difficulty, reason, data);
	}

	public static SpawnGroupData finalizeMobSpawnSpawner(Mob mob, ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason reason, SpawnGroupData data, Object spawner, boolean hasNoConfig) {
		return mob.finalizeSpawn(level, difficulty, reason, data);
	}

	public static boolean checkSpawnPositionSpawner(Mob mob, ServerLevelAccessor level, EntitySpawnReason reason, SpawnData spawnData, Object spawner) {
		return true;
	}

	public static boolean onEntityStruckByLightning(Entity victim, LightningBolt bolt) {
		return false;
	}

	public static boolean onProjectileImpact(Projectile projectile, HitResult result) {
		return false;
	}

	public static int onArrowLoose(ItemStack stack, Level level, Player player, int charge, boolean hasAmmo) {
		return charge;
	}

	public static boolean onEntityDestroyBlock(LivingEntity entity, BlockPos pos, BlockState state) {
		return state.getBlock().canEntityDestroy(state, entity.level(), pos, entity);
	}

	public static boolean canLivingConvert(LivingEntity oldEntity, EntityType<? extends LivingEntity> type, Consumer<Integer> timer) {
		return true;
	}

	public static void onLivingConvert(LivingEntity oldEntity, LivingEntity newEntity) {
	}

	public static boolean doPlayerHarvestCheck(Player player, BlockState state, Level level, BlockPos pos) {
		return player.hasCorrectToolForDrops(state);
	}
}
