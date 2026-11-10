package twilightforest.fabric;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import twilightforest.port.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import twilightforest.TwilightForestMod;

/**
 * Fabric port of NeoForge's complex entity spawn data: sent right after the vanilla add-entity packet (in the same
 * bundle) for entities implementing {@link IEntityWithComplexSpawn}.
 */
public record SpawnDataPayload(int entityId, byte[] data) implements CustomPacketPayload {
	public static final Type<SpawnDataPayload> TYPE = new Type<>(TwilightForestMod.prefix("spawn_data"));
	public static final StreamCodec<FriendlyByteBuf, SpawnDataPayload> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.VAR_INT, SpawnDataPayload::entityId,
		ByteBufCodecs.BYTE_ARRAY, SpawnDataPayload::data,
		SpawnDataPayload::new);

	public static SpawnDataPayload of(Entity entity) {
		RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), entity.registryAccess());
		((IEntityWithComplexSpawn) entity).writeSpawnData(buf);
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		return new SpawnDataPayload(entity.getId(), bytes);
	}

	public void apply(Entity entity) {
		if (entity instanceof IEntityWithComplexSpawn complex) {
			complex.readSpawnData(new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(this.data), entity.registryAccess()));
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
