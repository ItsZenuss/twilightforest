package twilightforest.port.neoforged.neoforge.event;

import net.minecraft.world.item.ItemStack;
import twilightforest.port.neoforged.bus.api.Event;
import twilightforest.port.neoforged.bus.api.ICancellableEvent;

public class AnvilUpdateEvent extends Event implements ICancellableEvent {
	private final ItemStack left;
	private final ItemStack right;
	private ItemStack output = ItemStack.EMPTY;
	private String name;
	private int cost;
	private int materialCost;
	private boolean canceled;

	public AnvilUpdateEvent(ItemStack left, ItemStack right, String name, int cost, int materialCost) {
		this.left = left;
		this.right = right;
		this.name = name;
		this.cost = cost;
		this.materialCost = materialCost;
	}

	public ItemStack getLeft() {
		return left;
	}

	public ItemStack getRight() {
		return right;
	}

	public ItemStack getOutput() {
		return output;
	}

	public void setOutput(ItemStack output) {
		this.output = output;
	}

	public String getName() {
		return name;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getMaterialCost() {
		return materialCost;
	}

	public void setMaterialCost(int materialCost) {
		this.materialCost = materialCost;
	}

	@Override
	public boolean isCanceled() {
		return canceled;
	}

	@Override
	public void setCanceled(boolean cancel) {
		this.canceled = cancel;
	}
}
