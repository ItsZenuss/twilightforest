package twilightforest.port.neoforged.neoforge.network;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;

public final class PacketDistributor {
	private PacketDistributor() {}

	public static void sendToPlayer(ServerPlayer player, CustomPacketPayload packet) {
		if (player != null && packet != null) {
			ServerPlayNetworking.send(player, packet);
		}
	}

	public static void sendToAllPlayers(CustomPacketPayload packet) {
		if (packet == null) return;
		net.minecraft.server.MinecraftServer server = twilightforest.port.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer();
		if (server == null) return;
		for (ServerPlayer player : PlayerLookup.all(server)) {
			ServerPlayNetworking.send(player, packet);
		}
	}

	public static void sendToPlayersNear(ServerLevel level, ServerPlayer excluded, double x, double y, double z, double radius, CustomPacketPayload packet) {
		if (level == null || packet == null) return;
		for (ServerPlayer player : PlayerLookup.around(level, new Vec3(x, y, z), radius)) {
			if (player != excluded) {
				ServerPlayNetworking.send(player, packet);
			}
		}
	}

	public static void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos pos, CustomPacketPayload packet) {
		if (level == null || packet == null) return;
		for (ServerPlayer player : PlayerLookup.tracking(level, pos)) {
			ServerPlayNetworking.send(player, packet);
		}
	}

	public static void sendToPlayersTrackingEntity(Entity entity, CustomPacketPayload packet) {
		if (entity == null || packet == null) return;
		for (ServerPlayer player : PlayerLookup.tracking(entity)) {
			ServerPlayNetworking.send(player, packet);
		}
	}

	public static void sendToPlayersTrackingEntityAndSelf(Entity entity, CustomPacketPayload packet) {
		if (entity == null || packet == null) return;
		for (ServerPlayer player : PlayerLookup.tracking(entity)) {
			ServerPlayNetworking.send(player, packet);
		}
		if (entity instanceof ServerPlayer player) {
			ServerPlayNetworking.send(player, packet);
		}
	}

	public static void sendToServer(CustomPacketPayload packet) {
		if (packet != null) {
			net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(packet);
		}
	}
}
