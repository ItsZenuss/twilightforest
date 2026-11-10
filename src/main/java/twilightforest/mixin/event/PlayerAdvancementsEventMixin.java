package twilightforest.mixin.event;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.entity.player.AdvancementEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/** Fabric port: fires AdvancementEvent.AdvancementEarnEvent when an advancement becomes complete. */
@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementsEventMixin {
	@Shadow
	private ServerPlayer player;

	@Shadow
	public abstract net.minecraft.advancements.AdvancementProgress getOrStartProgress(AdvancementHolder holder);

	@WrapMethod(method = "award")
	private boolean tf$earn(AdvancementHolder holder, String criterion, Operation<Boolean> original) {
		boolean wasDone = this.getOrStartProgress(holder).isDone();
		boolean result = original.call(holder, criterion);
		if (result && !wasDone && this.getOrStartProgress(holder).isDone()) {
			NeoForge.EVENT_BUS.post(new AdvancementEvent.AdvancementEarnEvent(this.player, holder));
		}
		return result;
	}
}
