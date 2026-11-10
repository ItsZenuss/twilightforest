package twilightforest;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.Minecraft;
import twilightforest.port.neoforged.bus.api.IEventBus;
import twilightforest.port.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import twilightforest.port.neoforged.neoforge.client.event.*;
import twilightforest.port.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import twilightforest.port.neoforged.neoforge.client.gui.map.RegisterMapDecorationRenderersEvent;
import twilightforest.port.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.RegisterTooltipAppendersEvent;
import twilightforest.port.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import twilightforest.port.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import twilightforest.port.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import twilightforest.port.neoforged.neoforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fabric client entrypoint. Client {@code @Component}s were already constructed (and their listeners attached) by
 * the main entrypoint, so this fires NeoForge's client registration events in the order NeoForge would, and
 * bridges client game events from Fabric callbacks.
 */
public final class TwilightForestClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(TwilightForestMod.ID + "-client");

	@Override
	public void onInitializeClient() {
		IEventBus modBus = TwilightForestMod.MOD_BUS;
		ServerLifecycleHooks.setClientRegistryAccess(() -> Minecraft.getInstance().level == null ? null : Minecraft.getInstance().level.registryAccess());

		modBus.post(new EntityRenderersEvent.RegisterLayerDefinitions());
		modBus.post(new EntityRenderersEvent.RegisterRenderers());
		LivingEntityRenderLayerRegistrationCallback.EVENT.register((type, renderer, helper, context) ->
			modBus.post(new EntityRenderersEvent.AddLayers(type, renderer, context)));

		modBus.post(new RegisterKeyMappingsEvent());
		modBus.post(new RegisterParticleProvidersEvent());
		modBus.post(new RegisterMenuScreensEvent());
		modBus.post(new RegisterColorHandlersEvent.BlockTintSources(null));
		modBus.post(new RegisterColorHandlersEvent.ItemTintSources());
		modBus.post(new RegisterItemModelsEvent());
		modBus.post(new RegisterConditionalItemModelPropertyEvent());
		modBus.post(new RegisterRangeSelectItemModelPropertyEvent());
		modBus.post(new RegisterSelectItemModelPropertyEvent());
		modBus.post(new RegisterSpecialModelRendererEvent());
		modBus.post(new RegisterBlockStateModels());
		modBus.post(new RegisterTextureAtlasesEvent());
		modBus.post(new AddClientReloadListenersEvent());
		modBus.post(new RegisterGuiLayersEvent());
		modBus.post(new RegisterClientTooltipComponentFactoriesEvent());
		modBus.post(new RegisterPictureInPictureRenderersEvent());
		modBus.post(new RegisterClientExtensionsEvent());
		modBus.post(new RegisterRenderStateModifiersEvent());
		modBus.post(new RegisterRenderPipelinesEvent());
		modBus.post(new RegisterCustomEnvironmentEffectRendererEvent());
		modBus.post(new RegisterMapDecorationRenderersEvent());
		modBus.post(new RegisterTooltipAppendersEvent());
		modBus.post(new RegisterDebugRenderersEvent());
		ModelLoadingPlugin.register(context -> modBus.post(new ModelEvent.RegisterStandalone(context)));

		modBus.post(new FMLClientSetupEvent());

		this.bridgeGameEvents();
		twilightforest.client.fabric.ClientRenderBridge.init();
		twilightforest.client.dev.DevClientSmokeTest.init();
	}

	private void bridgeGameEvents() {
		net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(twilightforest.fabric.TFDataMapLoader.SyncPayload.TYPE, (payload, context) ->
			twilightforest.fabric.TFDataMapLoader.applySync(payload, context.player().registryAccess(), context.client().hasSingleplayerServer()));
		net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.registerGlobalReceiver(twilightforest.fabric.SpawnDataPayload.TYPE, (payload, context) -> {
			if (context.client().level != null) {
				net.minecraft.world.entity.Entity entity = context.client().level.getEntity(payload.entityId());
				if (entity != null) payload.apply(entity);
			}
		});
		ClientTickEvents.START_CLIENT_TICK.register(client -> NeoForge.EVENT_BUS.post(new ClientTickEvent.Pre()));
		ClientTickEvents.END_CLIENT_TICK.register(client -> NeoForge.EVENT_BUS.post(new ClientTickEvent.Post()));

		ClientEntityEvents.ENTITY_LOAD.register((entity, level) -> {
			twilightforest.fabric.MultipartEntities.onLoad(entity, level);
			NeoForge.EVENT_BUS.post(new EntityJoinLevelEvent(entity, level));
		});
		ClientEntityEvents.ENTITY_UNLOAD.register((entity, level) -> {
			twilightforest.fabric.MultipartEntities.onUnload(entity, level);
			NeoForge.EVENT_BUS.post(new EntityLeaveLevelEvent(entity, level));
		});

		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			if (client.player != null) NeoForge.EVENT_BUS.post(new ClientPlayerNetworkEvent.LoggingIn(client.player));
		});
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> NeoForge.EVENT_BUS.post(new ClientPlayerNetworkEvent.LoggingOut(client.player)));

		ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> {
			RegisterTooltipAppendersEvent.appendAll(stack, context, flag, lines);
			NeoForge.EVENT_BUS.post(new ItemTooltipEvent(stack, lines));
		});

		ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
			NeoForge.EVENT_BUS.post(new ScreenEvent.Init.Post(screen));
			ScreenEvents.remove(screen).register(removed -> NeoForge.EVENT_BUS.post(new ScreenEvent.Closing(removed)));
		});
	}
}
