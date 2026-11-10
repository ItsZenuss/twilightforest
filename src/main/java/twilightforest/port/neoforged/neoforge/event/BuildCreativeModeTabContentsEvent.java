package twilightforest.port.neoforged.neoforge.event;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTabOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.List;

/**
 * Fabric port of NeoForge's BuildCreativeModeTabContentsEvent, fired from Fabric's MODIFY_OUTPUT_ALL event.
 */
public class BuildCreativeModeTabContentsEvent {
	private final ResourceKey<CreativeModeTab> tabKey;
	private final FabricCreativeModeTabOutput output;

	public BuildCreativeModeTabContentsEvent(ResourceKey<CreativeModeTab> tabKey, FabricCreativeModeTabOutput output) {
		this.tabKey = tabKey;
		this.output = output;
	}

	public ResourceKey<CreativeModeTab> getTabKey() {
		return this.tabKey;
	}

	public CreativeModeTab.ItemDisplayParameters getParameters() {
		return this.output.getContext();
	}

	public boolean hasPermissions() {
		return this.output.shouldShowOpRestrictedItems();
	}

	public void accept(ItemLike item) {
		this.output.accept(item);
	}

	public void accept(ItemStack stack) {
		this.output.accept(stack);
	}

	public void accept(ItemStack stack, CreativeModeTab.TabVisibility visibility) {
		this.output.accept(stack, visibility);
	}

	public void insertAfter(ItemStack after, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
		this.output.insertAfter(after, List.of(stack), visibility);
	}

	public void insertBefore(ItemStack before, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
		this.output.insertBefore(before, List.of(stack), visibility);
	}

	public void insertFirst(ItemStack stack, CreativeModeTab.TabVisibility visibility) {
		this.output.prepend(stack, visibility);
	}
}
