package twilightforest.mixin.event;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.entity.player.PlayerSpawnPhantomsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Fabric port: fires PlayerSpawnPhantomsEvent per player; DENY skips that player like a spectator. */
@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerEventMixin {
	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isSpectator()Z"))
	private boolean tf$phantoms(ServerPlayer player, Operation<Boolean> original) {
		if (original.call(player)) return true;
		return NeoForge.EVENT_BUS.post(new PlayerSpawnPhantomsEvent(player)).getResult() == PlayerSpawnPhantomsEvent.Result.DENY;
	}
}
