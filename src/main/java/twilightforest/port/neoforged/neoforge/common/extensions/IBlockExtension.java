package twilightforest.port.neoforged.neoforge.common.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import twilightforest.port.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

public interface IBlockExtension {

	default int getLightEmission(BlockState state, BlockGetter getter, BlockPos pos) {
		return state.getLightEmission();
	}

	@Nullable
	default BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
		return null;
	}

	default boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, ItemStack toolStack, boolean willHarvest, FluidState fluid) {
		return level.destroyBlock(pos, willHarvest, player);
	}

	default ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
		if (this instanceof net.minecraft.world.level.ItemLike itemLike) {
			return new ItemStack(itemLike);
		}
		return ItemStack.EMPTY;
	}

	default boolean onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
		return false;
	}

	default int getExpDrop(BlockState state, LevelReader level, RandomSource randomSource, BlockPos pos, int fortuneLevel, int silkTouchLevel) {
		return 0;
	}

	default boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
		return !state.requiresCorrectToolForDrops() || player.hasCorrectToolForDrops(state);
	}

	default boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.entity.Entity entity) {
		if (entity instanceof net.minecraft.world.entity.boss.enderdragon.EnderDragon)
			return !state.is(net.minecraft.tags.BlockTags.DRAGON_IMMUNE);
		if (entity instanceof net.minecraft.world.entity.boss.wither.WitherBoss || entity instanceof net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull)
			return state.isAir() || !state.is(net.minecraft.tags.BlockTags.WITHER_IMMUNE);
		return true;
	}

	default twilightforest.port.neoforged.neoforge.common.util.TriState canSustainPlant(BlockState state, BlockGetter level, BlockPos soilPosition, Direction facing, BlockState plant) {
		return twilightforest.port.neoforged.neoforge.common.util.TriState.DEFAULT;
	}

	default net.minecraft.world.level.block.SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable net.minecraft.world.entity.Entity entity) {
		return state.getSoundType();
	}

	default BlockState rotate(BlockState state, net.minecraft.world.level.LevelAccessor level, BlockPos pos, net.minecraft.world.level.block.Rotation direction) {
		return state.rotate(direction);
	}

	default boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
		return state.is(net.minecraft.tags.BlockTags.CLIMBABLE);
	}

	default float getFriction(BlockState state, LevelReader level, BlockPos pos, @Nullable net.minecraft.world.entity.Entity entity) {
		return state.getBlock().getFriction();
	}

	@Nullable
	default net.minecraft.world.level.pathfinder.PathType getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable net.minecraft.world.entity.Mob mob) {
		return null;
	}

	default float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.level.Explosion explosion) {
		return state.getBlock().getExplosionResistance();
	}

	default boolean addLandingEffects(BlockState state1, net.minecraft.server.level.ServerLevel level, BlockPos pos, BlockState state2, LivingEntity entity, int numberOfParticles) {
		return false;
	}

	default boolean addRunningEffects(BlockState state, Level level, BlockPos pos, net.minecraft.world.entity.Entity entity) {
		return false;
	}

	@Nullable
	default Integer getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos) {
		return null;
	}

	default boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
		return false;
	}

	default boolean isFireSource(BlockState state, LevelReader level, BlockPos pos, Direction direction) {
		return false;
	}

	default boolean isStickyBlock(BlockState state) {
		return false;
	}

	default boolean canStickTo(BlockState state, BlockState other) {
		return true;
	}

	default boolean isScaffolding(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
		return false;
	}
}
