package twilightforest.port.neoforged.neoforge.network.registration;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import twilightforest.port.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class PayloadRegistrar {
	private final String namespace;

	public PayloadRegistrar(String namespace) {
		this.namespace = namespace;
	}

	public PayloadRegistrar versioned(String version) {
		return this;
	}

	public PayloadRegistrar optional() {
		return this;
	}

	public <T extends CustomPacketPayload> PayloadRegistrar playToClient(
		CustomPacketPayload.Type<T> type,
		StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
		BiConsumer<T, IPayloadContext> handler
	) {
		PayloadTypeRegistry.clientboundPlay().register(type, codec);
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			registerClientReceiver(type, handler);
		}
		return this;
	}

	public <T extends CustomPacketPayload> PayloadRegistrar playToServer(
		CustomPacketPayload.Type<T> type,
		StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
		BiConsumer<T, IPayloadContext> handler
	) {
		PayloadTypeRegistry.serverboundPlay().register(type, codec);
		ServerPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
			IPayloadContext ctx = new IPayloadContext() {
				@Override
				public Player player() {
					return context.player();
				}

				@Override
				public PacketFlow flow() {
					return PacketFlow.SERVERBOUND;
				}

				@Override
				public CompletableFuture<Void> enqueueWork(Runnable runnable) {
					return CompletableFuture.runAsync(runnable, context.server());
				}
			};
			handler.accept(payload, ctx);
		});
		return this;
	}

	public <T extends CustomPacketPayload> PayloadRegistrar playBidirectional(
		CustomPacketPayload.Type<T> type,
		StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
		BiConsumer<T, IPayloadContext> clientHandler,
		BiConsumer<T, IPayloadContext> serverHandler
	) {
		playToClient(type, codec, clientHandler);
		playToServer(type, codec, serverHandler);
		return this;
	}

	private <T extends CustomPacketPayload> void registerClientReceiver(
		CustomPacketPayload.Type<T> type,
		BiConsumer<T, IPayloadContext> handler
	) {
		ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
			IPayloadContext ctx = new IPayloadContext() {
				@Override
				public Player player() {
					return context.player();
				}

				@Override
				public PacketFlow flow() {
					return PacketFlow.CLIENTBOUND;
				}

				@Override
				public CompletableFuture<Void> enqueueWork(Runnable runnable) {
					return CompletableFuture.runAsync(runnable, context.client());
				}
			};
			handler.accept(payload, ctx);
		});
	}
}
