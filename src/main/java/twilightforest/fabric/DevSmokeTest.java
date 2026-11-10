package twilightforest.fabric;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDimension;

import java.util.HashMap;
import java.util.Map;

/**
 * Development-only check, enabled with {@code -Dtwilightforest.smoketest=true} (set by the "gametest" run config):
 * once the server starts, generates a patch of the Twilight Forest so terrain, biome and feature code runs, and
 * logs a summary. Does nothing in normal play.
 */
public final class DevSmokeTest {
	private DevSmokeTest() {}

	public static void init() {
		if (!Boolean.getBoolean("twilightforest.smoketest")) return;
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			// Force every mixin target to load so a broken injection point fails here instead of mid-game.
			org.spongepowered.asm.mixin.MixinEnvironment.getCurrentEnvironment().audit();
			TwilightForestMod.LOGGER.info("[smoketest] Mixin audit finished");
			ServerLevel level = server.getLevel(TFDimension.DIMENSION_KEY);
			if (level == null) {
				TwilightForestMod.LOGGER.error("[smoketest] Twilight Forest dimension is missing");
				return;
			}
			int radius = Integer.getInteger("twilightforest.smoketest.radius", 6);
			Map<String, Integer> biomes = new HashMap<>();
			long start = System.currentTimeMillis();
			int chunks = 0;
			for (int x = -radius; x <= radius; x++) {
				for (int z = -radius; z <= radius; z++) {
					ChunkAccess chunk = level.getChunk(x, z);
					chunks++;
					BlockPos pos = new BlockPos((x << 4) + 8, 0, (z << 4) + 8);
					int y = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX() & 15, pos.getZ() & 15);
					String biome = level.getBiome(pos.atY(y)).unwrapKey().map(k -> k.identifier().toString()).orElse("?");
					biomes.merge(biome, 1, Integer::sum);
				}
			}
			TwilightForestMod.LOGGER.info("[smoketest] Generated {} Twilight Forest chunks in {} ms; surface biomes: {}", chunks, System.currentTimeMillis() - start, biomes);
		});
		if (Boolean.getBoolean("twilightforest.smoketest.teleport")) {
			net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> server.execute(() -> {
				ServerLevel level = server.getLevel(TFDimension.DIMENSION_KEY);
				if (level == null) return;
				int y = level.getChunk(0, 0).getHeight(Heightmap.Types.MOTION_BLOCKING, 8, 8) + 2;
				handler.getPlayer().teleportTo(level, 8.5, y, 8.5, java.util.Set.of(), 135.0F, 20.0F, true);
				TwilightForestMod.LOGGER.info("[smoketest] Teleported {} into the Twilight Forest at y={}", handler.getPlayer().getName().getString(), y);
				if (Boolean.getBoolean("twilightforest.smoketest.showcase")) showcase(level, new BlockPos(8, y, 8));
				if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("trinkets")) {
					// trinket slots are set up after login, so equip a couple of seconds later
					net.minecraft.server.level.ServerPlayer player = handler.getPlayer();
					int[] delay = {40};
					net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.END_SERVER_TICK.register(s -> {
						if (delay[0] >= 0 && --delay[0] == 0) twilightforest.compat.trinkets.TrinketsCompat.equipForSmokeTest(player);
					});
				}
			}));
		}
	}

	/**
	 * Places every Twilight Forest block on a platform and spawns one of every Twilight Forest entity next to the
	 * player, so their code and renderers run. Failures are logged per block/entity.
	 */
	private static void showcase(ServerLevel level, BlockPos origin) {
		int placed = 0, failedBlocks = 0, spawned = 0, failedEntities = 0;
		BlockPos base = origin.offset(4, -1, 4);
		java.util.List<net.minecraft.world.level.block.Block> blocks = net.minecraft.core.registries.BuiltInRegistries.BLOCK.stream()
			.filter(b -> net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(b).getNamespace().equals(TwilightForestMod.ID)).toList();
		int side = (int) Math.ceil(Math.sqrt(blocks.size()));
		for (int i = 0; i < blocks.size(); i++) {
			BlockPos pos = base.offset((i % side) * 2, 0, (i / side) * 2);
			try {
				level.setBlock(pos.below(), net.minecraft.world.level.block.Blocks.STONE.defaultBlockState(), 2);
				level.setBlock(pos, blocks.get(i).defaultBlockState(), 2);
				placed++;
			} catch (Throwable t) {
				failedBlocks++;
				TwilightForestMod.LOGGER.error("[smoketest] Failed placing {}", net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(blocks.get(i)), t);
			}
		}
		java.util.List<net.minecraft.world.entity.EntityType<?>> types = net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.stream()
			.filter(t -> net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getKey(t).getNamespace().equals(TwilightForestMod.ID)).toList();
		for (int i = 0; i < types.size(); i++) {
			double angle = i * (Math.PI * 2 / types.size());
			BlockPos pos = origin.offset((int) (Math.cos(angle) * 10), 2, (int) (Math.sin(angle) * 10));
			try {
				net.minecraft.world.entity.Entity entity = types.get(i).spawn(level, pos, net.minecraft.world.entity.EntitySpawnReason.COMMAND);
				if (entity != null) {
					spawned++;
					if (entity instanceof net.minecraft.world.entity.Mob mob) mob.setNoAi(false);
				}
			} catch (Throwable t) {
				failedEntities++;
				TwilightForestMod.LOGGER.error("[smoketest] Failed spawning {}", net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getKey(types.get(i)), t);
			}
		}
		TwilightForestMod.LOGGER.info("[smoketest] Showcase: placed {}/{} blocks ({} failed), spawned {}/{} entities ({} failed)",
			placed, blocks.size(), failedBlocks, spawned, types.size(), failedEntities);
	}
}
