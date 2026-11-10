package twilightforest.mixin.event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.level.GameRuleChangedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Fabric port: fires GameRuleChangedEvent after a game rule changes. */
@Mixin(GameRules.class)
public abstract class GameRulesEventMixin {
	@Inject(method = "set", at = @At("TAIL"))
	private <T> void tf$changed(GameRule<T> rule, T value, MinecraftServer server, CallbackInfo ci) {
		if (server != null) NeoForge.EVENT_BUS.post(new GameRuleChangedEvent(rule, server));
	}
}
