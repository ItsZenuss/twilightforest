package twilightforest.fabric;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.CommonLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.world.InteractionResult;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.AddServerReloadListenersEvent;
import twilightforest.port.neoforged.neoforge.event.OnDatapackSyncEvent;
import twilightforest.port.neoforged.neoforge.event.RegisterCommandsEvent;
import twilightforest.port.neoforged.neoforge.event.TagsUpdatedEvent;
import twilightforest.port.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import twilightforest.port.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import twilightforest.port.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import twilightforest.port.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import twilightforest.port.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import twilightforest.port.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import twilightforest.port.neoforged.neoforge.event.entity.player.PlayerEvent;
import twilightforest.port.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import twilightforest.port.neoforged.neoforge.event.level.block.BreakBlockEvent;

/**
 * Fires NeoForge game events on {@link NeoForge#EVENT_BUS} from Fabric API callbacks. Events Fabric has no hook
 * for are fired by mixins in {@code twilightforest.mixin.event}.
 */
public final class NeoForgeEventBridge {
	private static boolean initialized;

	private NeoForgeEventBridge() {}

	public static void init() {
		if (initialized) return;
		initialized = true;

		// Fabric reload listeners must be registered during init; the shim forwards straight to Fabric.
		NeoForge.EVENT_BUS.post(new AddServerReloadListenersEvent());

		CommandRegistrationCallback.EVENT.register((dispatcher, context, selection) ->
			NeoForge.EVENT_BUS.post(new RegisterCommandsEvent(dispatcher, context, selection)));

		ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) ->
			NeoForge.EVENT_BUS.post(new OnDatapackSyncEvent(player, player.level().getServer().getPlayerList())));
		CommonLifecycleEvents.TAGS_LOADED.register((registries, client) -> NeoForge.EVENT_BUS.post(new TagsUpdatedEvent()));

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
			NeoForge.EVENT_BUS.post(new PlayerEvent.PlayerLoggedInEvent(handler.getPlayer())));
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
			NeoForge.EVENT_BUS.post(new PlayerEvent.PlayerLoggedOutEvent(handler.getPlayer())));
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) ->
			NeoForge.EVENT_BUS.post(new PlayerEvent.Clone(newPlayer, oldPlayer, !alive)));
		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) ->
			NeoForge.EVENT_BUS.post(new PlayerEvent.PlayerRespawnEvent(newPlayer, alive)));

		ServerEntityEvents.ALLOW_LOAD.register((entity, level, reason, loadedFromDisk) ->
			!NeoForge.EVENT_BUS.post(new EntityJoinLevelEvent(entity, level, loadedFromDisk)).isCanceled());
		ServerEntityEvents.ENTITY_UNLOAD.register((entity, level) -> {
			MultipartEntities.onUnload(entity, level);
			NeoForge.EVENT_BUS.post(new EntityLeaveLevelEvent(entity, level));
		});
		ServerEntityEvents.ENTITY_LOAD.register(MultipartEntities::onLoad);
		ServerEntityEvents.EQUIPMENT_CHANGE.register((entity, slot, from, to) ->
			NeoForge.EVENT_BUS.post(new LivingEquipmentChangeEvent(entity, slot, from, to)));

		ServerLivingEntityEvents.ALLOW_DEATH.register((entity, source, amount) ->
			!NeoForge.EVENT_BUS.post(new LivingDeathEvent(entity, source)).isCanceled());
		ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseTaken, taken, blocked) -> {
			if (!blocked) NeoForge.EVENT_BUS.post(new LivingDamageEvent.Post(entity, source, taken));
		});

		AttackEntityCallback.EVENT.register((player, level, hand, entity, hit) ->
			NeoForge.EVENT_BUS.post(new AttackEntityEvent(player, entity)).isCanceled() ? InteractionResult.FAIL : InteractionResult.PASS);
		UseBlockCallback.EVENT.register((player, level, hand, hit) -> {
			PlayerInteractEvent.RightClickBlock event = NeoForge.EVENT_BUS.post(new PlayerInteractEvent.RightClickBlock(player, hand, hit.getBlockPos(), hit));
			if (event.isCanceled()) {
				InteractionResult result = event.getCancellationResult();
				return result != null ? result : InteractionResult.FAIL;
			}
			return InteractionResult.PASS;
		});
		PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockEntity) ->
			!NeoForge.EVENT_BUS.post(new BreakBlockEvent(level, pos, state, player)).isCanceled());
	}
}
