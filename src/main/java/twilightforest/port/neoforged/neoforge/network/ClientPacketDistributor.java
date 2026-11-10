package twilightforest.port.neoforged.neoforge.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public final class ClientPacketDistributor {
	private ClientPacketDistributor() {}

	public static void sendToServer(CustomPacketPayload packet) {
		if (packet != null) {
			ClientPlayNetworking.send(packet);
		}
	}
}
