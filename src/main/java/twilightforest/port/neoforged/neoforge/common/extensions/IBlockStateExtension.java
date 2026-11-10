package twilightforest.port.neoforged.neoforge.common.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.port.neoforged.neoforge.common.util.TriState;
import org.jetbrains.annotations.Nullable;

/**
 * Fabric port of NeoForge's IBlockStateExtension. Injected into BlockState by Loom (compile time) and by
 * {@code twilightforest.mixin.compat.BlockStateExtensionMixin} (runtime). Each method forwards to the block.
 */
public interface IBlockStateExtension {
	private BlockState self() {
		return (BlockState) (Object) this;
	}

	default boolean canEntityDestroy(BlockGetter level, BlockPos pos, Entity entity) {
		return this.self().getBlock().canEntityDestroy(this.self(), level, pos, entity);
	}

	default boolean canHarvestBlock(BlockGetter level, BlockPos pos, Player player) {
		return this.self().getBlock().canHarvestBlock(this.self(), level, pos, player);
	}

	default boolean isEmpty() {
		return this.self().isAir();
	}

	default BlockState rotate(LevelAccessor level, BlockPos pos, Rotation direction) {
		return this.self().getBlock().rotate(this.self(), level, pos, direction);
	}

	default SoundType getSoundType(LevelReader level, BlockPos pos, @Nullable Entity entity) {
		return this.self().getBlock().getSoundType(this.self(), level, pos, entity);
	}

	default TriState canSustainPlant(BlockGetter level, BlockPos soilPosition, Direction facing, BlockState plant) {
		return this.self().getBlock().canSustainPlant(this.self(), level, soilPosition, facing, plant);
	}

	default float getFriction(LevelReader level, BlockPos pos, @Nullable Entity entity) {
		return this.self().getBlock().getFriction(this.self(), level, pos, entity);
	}
}
