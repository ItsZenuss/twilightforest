package twilightforest.port.neoforged.neoforge.client.event;

import net.minecraft.world.item.crafting.RecipeMap;
import twilightforest.port.neoforged.bus.api.Event;

public class RecipesReceivedEvent extends Event {
	private final RecipeMap recipeMap;

	public RecipesReceivedEvent() {
		this(RecipeMap.EMPTY);
	}

	public RecipesReceivedEvent(RecipeMap recipeMap) {
		this.recipeMap = recipeMap;
	}

	public RecipeMap getRecipeMap() {
		return recipeMap;
	}
}
