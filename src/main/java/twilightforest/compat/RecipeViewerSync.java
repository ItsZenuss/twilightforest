package twilightforest.compat;

import net.minecraft.world.item.crafting.RecipeType;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.OnDatapackSyncEvent;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import twilightforest.init.TFRecipes;

@Component
public class RecipeViewerSync {

	@PostConstruct
	private void setup() {
		NeoForge.EVENT_BUS.addListener(OnDatapackSyncEvent.class, this::requestRecipeSync);
	}

	private void requestRecipeSync(OnDatapackSyncEvent event) {
		event.sendRecipes(RecipeType.CRAFTING, TFRecipes.UNCRAFTING_RECIPE.get(), TFRecipes.DRYING_RECIPE.get());
	}
}
