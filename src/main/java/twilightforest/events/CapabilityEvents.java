package twilightforest.events;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.LevelData;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import twilightforest.port.neoforged.neoforge.event.entity.player.PlayerEvent;
import twilightforest.port.neoforged.neoforge.event.tick.EntityTickEvent;
import twilightforest.port.neoforged.neoforge.event.tick.PlayerTickEvent;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import twilightforest.components.entity.FortificationShieldAttachment;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDimension;
import twilightforest.world.NoReturnTeleporter;
import twilightforest.world.TFTeleporter;

@Component
public class CapabilityEvents {

	@PostConstruct
	private void setup() {
		NeoForge.EVENT_BUS.addListener(EntityTickEvent.Post.class, this::updateShields);
		NeoForge.EVENT_BUS.addListener(PlayerTickEvent.Post.class, this::updatePlayerCaps);
		NeoForge.EVENT_BUS.addListener(LivingIncomingDamageEvent.class, this::absorbShieldHits);
		NeoForge.EVENT_BUS.addListener(PlayerEvent.PlayerRespawnEvent.class, this::spawnInTFIfNecessary);
		NeoForge.EVENT_BUS.addListener(PlayerEvent.PlayerLoggedInEvent.class, this::playerLogsIn);
	}

	private void updateShields(EntityTickEvent.Post event) {
		if (event.getEntity() instanceof LivingEntity living && !living.level().isClientSide() && living.hasData(TFDataAttachments.FORTIFICATION_SHIELDS)) {
			event.getEntity().getData(TFDataAttachments.FORTIFICATION_SHIELDS).tick(living);
		}
	}

	private void updatePlayerCaps(PlayerTickEvent.Post event) {
		if (event.getEntity().getData(TFDataAttachments.FEATHER_FAN)) {
			event.getEntity().setIgnoreFallDamageFromCurrentImpulse(true, event.getEntity().position());
			event.getEntity().currentImpulseImpactPos = event.getEntity().position();

			if (event.getEntity().onGround() || event.getEntity().isSwimming() || event.getEntity().isInWater()) {
				event.getEntity().setData(TFDataAttachments.FEATHER_FAN, false);
			}
		}
		event.getEntity().getData(TFDataAttachments.YETI_THROWING).tick(event.getEntity());
		event.getEntity().getData(TFDataAttachments.TF_PORTAL_COOLDOWN).tick(event.getEntity());
	}

	private void absorbShieldHits(LivingIncomingDamageEvent event) {
		LivingEntity living = event.getEntity();
		// shields
		if (!living.level().isClientSide() && !event.getSource().is(DamageTypeTags.BYPASSES_ARMOR)) {
            FortificationShieldAttachment attachment = living.getData(TFDataAttachments.FORTIFICATION_SHIELDS);
			if (attachment.shieldsLeft() > 0) {
				if (living.invulnerableTime <= 0) {
					attachment.breakShield(living, false);
					FortificationShieldAttachment.addShieldBreakParticles(event.getSource(), living);
					living.invulnerableTime = 20; //invulnerableDuration was only a static constant.
				}
				event.setCanceled(true);
			}
		}
	}

	private void spawnInTFIfNecessary(PlayerEvent.PlayerRespawnEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;

		if (serverPlayer.getRespawnConfig() == null) {
			newSpawnInTwilightForest(serverPlayer);
		}
	}

	/**
	 * When player logs in, report conflict status, set progression status
	 */
	public void playerLogsIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity().level().isClientSide() || !(event.getEntity() instanceof ServerPlayer player))
			return;
		dataFixLegacyBanish(player);
		if (!player.hasData(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST))
			newSpawnInTwilightForest(player);
	}

	private static void newSpawnInTwilightForest(ServerPlayer player) {
		if (!TFConfig.newPlayersSpawnInTF)
			return;
		ServerLevel level = player.level().getServer().getLevel(TFDimension.DIMENSION_KEY);
		if (level == null)
			return;

		BlockPos newDefaultSpawn = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, player.blockPosition());

		player.teleport(TFConfig.portalForNewPlayerSpawn ?
			TFTeleporter.createTransition(player, level, newDefaultSpawn, true) :
			NoReturnTeleporter.createNoPortalTransition(level, player, newDefaultSpawn));
		ServerPlayer.RespawnConfig newSpawn = new ServerPlayer.RespawnConfig(
			LevelData.RespawnData.of(TFDimension.DIMENSION_KEY, newDefaultSpawn, player.getYRot(), player.getXRot()), true);
		player.setRespawnPosition(newSpawn, false);

		player.setData(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST, Unit.INSTANCE);
	}

	public static final String PERSISTED_NBT_TAG = "PlayerPersisted";

	private static void dataFixLegacyBanish(ServerPlayer player) {
		CompoundTag tagCompound = player.getPersistentData();
		if (!tagCompound.contains(PERSISTED_NBT_TAG))
			return;
		CompoundTag playerData = tagCompound.getCompoundOrEmpty(PERSISTED_NBT_TAG);
		if (!playerData.contains("twilightforest_banished"))
			return;

		playerData.remove("twilightforest_banished");
		tagCompound.put(PERSISTED_NBT_TAG, playerData);

		if (player.hasData(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST))
			return;

		player.setData(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST, Unit.INSTANCE);
	}
}
