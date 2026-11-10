package twilightforest.mixin.hooks;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import twilightforest.asmhooks.PlayerHooks;

/**
 * Fabric port of the NeoForge coremod transformer {@code twilightforest.asm.transformers.player.ReduceMovementFoodExhaustionTransformer}.
 */
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerHooksMixin {
	@ModifyArg(method = {"checkMovementStatistics", "jumpFromGround"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;causeFoodExhaustion(F)V"), require = 0)
	private float tf$exhaustion(float amount) {
		return PlayerHooks.getFoodExhaustion(amount, (ServerPlayer) (Object) this);
	}

	@ModifyArg(method = {"checkMovementStatistics", "jumpFromGround"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;causeFoodExhaustion(F)V"), require = 0)
	private float tf$exhaustionSuper(float amount) {
		return PlayerHooks.getFoodExhaustion(amount, (ServerPlayer) (Object) this);
	}
}
