package twilightforest.client.fabric;

import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.renderer.culling.Frustum;
import twilightforest.port.neoforged.neoforge.client.event.ExtractBlockOutlineRenderStateEvent;
import twilightforest.port.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;
import twilightforest.port.neoforged.neoforge.client.event.RenderLevelStageEvent;
import twilightforest.port.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

/**
 * Fabric port: fires NeoForge's level rendering events from Fabric's LevelRenderEvents.
 */
public final class ClientRenderBridge {
	@Nullable
	public static Frustum currentFrustum;
	@Nullable
	public static net.minecraft.client.renderer.state.level.LevelRenderState lastLevelState;

	/**
	 * NeoForge picked custom sky/weather renderers through dimension type attributes; on Fabric the Twilight Forest
	 * dimension is mapped to its renderers here.
	 */
	@Nullable
	public static twilightforest.port.neoforged.neoforge.client.CustomSkyboxRenderer skyRenderer() {
		net.minecraft.client.multiplayer.ClientLevel level = net.minecraft.client.Minecraft.getInstance().level;
		if (level == null || !level.dimension().equals(twilightforest.init.TFDimension.DIMENSION_KEY)) return null;
		return twilightforest.port.neoforged.neoforge.client.CustomEnvironmentEffectsRendererManager.getCustomSkyboxRenderer(twilightforest.client.TwilightForestRenderInfo.SKY_RENDERER);
	}

	@Nullable
	public static twilightforest.port.neoforged.neoforge.client.CustomWeatherEffectRenderer weatherRenderer() {
		net.minecraft.client.multiplayer.ClientLevel level = net.minecraft.client.Minecraft.getInstance().level;
		if (level == null || !level.dimension().equals(twilightforest.init.TFDimension.DIMENSION_KEY)) return null;
		return twilightforest.port.neoforged.neoforge.client.CustomEnvironmentEffectsRendererManager.getCustomWeatherEffectRenderer(twilightforest.client.TwilightForestRenderInfo.WEATHER_RENDERER);
	}

	private ClientRenderBridge() {}

	private static final net.minecraft.resources.Identifier[] VANILLA_HUD = {
		net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements.MISC_OVERLAYS, net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements.CROSSHAIR,
		net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements.HOTBAR, net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements.ARMOR_BAR,
		net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements.HEALTH_BAR, net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements.FOOD_BAR,
		net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements.AIR_BAR, net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements.MOUNT_HEALTH,
		net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements.INFO_BAR, net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements.EXPERIENCE_LEVEL,
		net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements.MOB_EFFECTS, net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements.BOSS_BAR
	};

	public static void init() {
		// NeoForge's RenderGuiLayerEvent.Pre: wrap vanilla HUD elements so listeners can cancel them.
		for (net.minecraft.resources.Identifier id : VANILLA_HUD) {
			net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry.replaceElement(id, element -> (graphics, delta) -> {
				if (!NeoForge.EVENT_BUS.post(new twilightforest.port.neoforged.neoforge.client.event.RenderGuiLayerEvent.Pre(id)).isCanceled()) {
					element.extractRenderState(graphics, delta);
				}
			});
		}

		LevelRenderEvents.END_EXTRACTION.register(ctx -> {
			lastLevelState = ctx.levelState();
			NeoForge.EVENT_BUS.post(new ExtractLevelRenderStateEvent(ctx.levelState(), ctx.level(), ctx.camera(), ctx.deltaTracker(), currentFrustum, ctx.levelRenderer()));
		});

		LevelRenderEvents.COLLECT_SUBMITS.register(ctx -> NeoForge.EVENT_BUS.post(new SubmitCustomGeometryEvent(ctx.levelState(), ctx.poseStack(), ctx.submitNodeCollector())));

		LevelRenderEvents.END_MAIN.register(ctx -> NeoForge.EVENT_BUS.post(new RenderLevelStageEvent.AfterWeather(ctx.levelState(), ctx.poseStack())));

		LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register((ctx, outline) -> {
			net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
			ExtractBlockOutlineRenderStateEvent event = NeoForge.EVENT_BUS.post(new ExtractBlockOutlineRenderStateEvent(outline.pos(), mc.gameRenderer.getMainCamera()));
			if (event.isCanceled()) return false;
			boolean replaced = false;
			for (ExtractBlockOutlineRenderStateEvent.CustomOutlineRenderer renderer : event.getCustomRenderers()) {
				replaced |= renderer.render(outline, ctx.bufferSource(), ctx.poseStack(), outline.isTranslucent(), ctx.levelState());
			}
			return !replaced;
		});
	}
}
