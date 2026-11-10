package twilightforest.port.neoforged.neoforge.common.extensions;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;

public interface IRecipeManagerExtension {
	default RecipeMap recipeMap() {
		if (this instanceof RecipeManager manager) {
			return RecipeMap.create(manager.getRecipes());
		}
		return RecipeMap.EMPTY;
	}
}
