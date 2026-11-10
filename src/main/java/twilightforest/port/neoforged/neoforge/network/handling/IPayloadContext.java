package twilightforest.port.neoforged.neoforge.network.handling;

import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.CompletableFuture;

public interface IPayloadContext {
	Player player();

	PacketFlow flow();

	CompletableFuture<Void> enqueueWork(Runnable runnable);
}
