package twilightforest.mixin.ext;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import twilightforest.port.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.fabric.NeoHooks;
import twilightforest.fabric.SpawnDataPayload;

import java.util.function.Consumer;

/**
 * Fabric port: NeoForge's IEntityWithComplexSpawn spawn data and IEntityExtension#sendPairingData, both sent with the
 * entity's pairing packets.
 */
@Mixin(ServerEntity.class)
public abstract class ServerEntityMixin {
	@Shadow
	@Final
	private Entity entity;

	@Inject(method = "sendPairingData", at = @At("TAIL"))
	@SuppressWarnings("unchecked")
	private void tf$extraPairingData(ServerPlayer player, Consumer<Packet<ClientGamePacketListener>> consumer, CallbackInfo ci) {
		if (this.entity instanceof IEntityWithComplexSpawn) {
			consumer.accept((Packet<ClientGamePacketListener>) (Packet<?>) ServerPlayNetworking.createClientboundPacket(SpawnDataPayload.of(this.entity)));
		}
		if (NeoHooks.overrides(this.entity, "sendPairingData", 2)) {
			this.entity.sendPairingData(player, payload -> consumer.accept((Packet<ClientGamePacketListener>) (Packet<?>) ServerPlayNetworking.createClientboundPacket(payload)));
		}
	}
}
