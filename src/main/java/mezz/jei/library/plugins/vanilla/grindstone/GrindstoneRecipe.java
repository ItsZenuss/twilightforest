package mezz.jei.library.plugins.vanilla.grindstone;

import mezz.jei.api.recipe.vanilla.IJeiGrindstoneRecipe;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GrindstoneRecipe implements IJeiGrindstoneRecipe {
	private final List<ItemStack> topInputs;
	private final List<ItemStack> bottomInputs;
	private final List<ItemStack> outputs;
	private final int minXpReward;
	private final int maxXpReward;
	private final Identifier uid;

	public GrindstoneRecipe(List<ItemStack> topInputs, List<ItemStack> bottomInputs, List<ItemStack> outputs, int minXpReward, int maxXpReward, @Nullable Identifier uid) {
		this.topInputs = topInputs;
		this.bottomInputs = bottomInputs;
		this.outputs = outputs;
		this.minXpReward = minXpReward;
		this.maxXpReward = maxXpReward;
		this.uid = uid;
	}

	@Override
	public List<ItemStack> getTopInputs() {
		return topInputs;
	}

	@Override
	public List<ItemStack> getBottomInputs() {
		return bottomInputs;
	}

	@Override
	public List<ItemStack> getOutputs() {
		return outputs;
	}

	@Override
	public int getMinXpReward() {
		return minXpReward;
	}

	@Override
	public int getMaxXpReward() {
		return maxXpReward;
	}

	@Override
	public Identifier getUid() {
		return uid;
	}

	@Override
	public boolean isOutputRenderOnly() {
		return false;
	}
}
