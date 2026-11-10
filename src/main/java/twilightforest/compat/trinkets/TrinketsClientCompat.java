package twilightforest.compat.trinkets;

import com.google.common.reflect.TypeToken;
import eu.pb4.trinkets.api.client.TrinketRendererRegistry;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.ItemLike;
import twilightforest.port.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import twilightforest.port.neoforged.neoforge.client.event.EntityRenderersEvent;
import twilightforest.port.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import twilightforest.asmhooks.RenderHooks;
import twilightforest.block.AbstractTrophyBlock;
import twilightforest.client.model.TFModelLayers;
import twilightforest.compat.trinkets.model.CharmOfLifeNecklaceModel;
import twilightforest.compat.trinkets.renderer.CharmOfKeepingRenderer;
import twilightforest.compat.trinkets.renderer.CharmOfLifeNecklaceRenderer;
import twilightforest.compat.trinkets.renderer.TrinketHeadRenderer;
import twilightforest.init.TFItems;

/**
 * Client half of {@link TrinketsCompat}: renderers for worn trinkets, and hiding the head under a worn trophy.
 */
public class TrinketsClientCompat {

	public static void registerTrinketLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(TFModelLayers.CHARM_OF_LIFE, CharmOfLifeNecklaceModel::create);
	}

	public static void registerTrinketRenderers(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_LIFE_1.get(), new CharmOfLifeNecklaceRenderer(ARGB.colorFromFloat(1.0F, 1.0F, 0.5F, 0.5F)));
			TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_LIFE_2.get(), new CharmOfLifeNecklaceRenderer(ARGB.colorFromFloat(1.0F, 1.0F, 0.9F, 0.0F)));
			CharmOfKeepingRenderer keeping = new CharmOfKeepingRenderer();
			TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_KEEPING_1.get(), keeping);
			TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_KEEPING_2.get(), keeping);
			TrinketRendererRegistry.registerRenderer(TFItems.CHARM_OF_KEEPING_3.get(), keeping);

			TrinketHeadRenderer head = new TrinketHeadRenderer();
			for (ItemLike item : TrinketsCompat.wearables()) {
				if (!TrinketRendererRegistry.hasRenderer(item.asItem())) {
					TrinketRendererRegistry.registerRenderer(item.asItem(), head);
				}
			}
		});
	}

	public static void registerCustomRenderData(RegisterRenderStateModifiersEvent event) {
		event.registerEntityModifier(new TypeToken<LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>>() {
		}, (LivingEntity living, LivingEntityRenderState state) -> {
			if (TrinketsCompat.isTrinketEquippedAndVisible(living, itemStack -> itemStack.getItem() instanceof BlockItem head && head.getBlock() instanceof AbstractTrophyBlock)) {
				state.setRenderData(RenderHooks.HIDE_HEAD_KEY, true);
			}
		});
	}
}
