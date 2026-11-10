package twilightforest.port.neoforged.neoforge.event;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import twilightforest.port.neoforged.bus.api.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Fabric port of NeoForge's ItemAttributeModifierEvent. Holds a mutable copy of the stack's attribute modifiers;
 * {@link #build()} returns the result.
 */
public class ItemAttributeModifierEvent extends Event {
	private final ItemStack itemStack;
	private final ItemAttributeModifiers defaultModifiers;
	private final List<ItemAttributeModifiers.Entry> entries;

	public ItemAttributeModifierEvent(ItemStack itemStack) {
		this(itemStack, itemStack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY));
	}

	public ItemAttributeModifierEvent(ItemStack itemStack, ItemAttributeModifiers defaultModifiers) {
		this.itemStack = itemStack;
		this.defaultModifiers = defaultModifiers;
		this.entries = new ArrayList<>(defaultModifiers.modifiers());
	}

	public ItemStack getItemStack() {
		return this.itemStack;
	}

	public ItemAttributeModifiers getDefaultModifiers() {
		return this.defaultModifiers;
	}

	public List<ItemAttributeModifiers.Entry> getModifiers() {
		return List.copyOf(this.entries);
	}

	public void clearModifiers() {
		this.entries.clear();
	}

	public boolean removeModifier(Holder<Attribute> attribute, net.minecraft.resources.Identifier id) {
		return this.entries.removeIf(e -> e.attribute().equals(attribute) && e.modifier().id().equals(id));
	}

	public void replaceModifier(Holder<Attribute> attribute, AttributeModifier modifier, EquipmentSlotGroup slot) {
		this.removeModifier(attribute, modifier.id());
		this.entries.add(new ItemAttributeModifiers.Entry(attribute, modifier, slot));
	}

	public boolean addModifier(Holder<Attribute> attribute, AttributeModifier modifier, EquipmentSlotGroup slot) {
		for (ItemAttributeModifiers.Entry e : this.entries) {
			if (e.attribute().equals(attribute) && e.modifier().id().equals(modifier.id()))
				return false;
		}
		this.entries.add(new ItemAttributeModifiers.Entry(attribute, modifier, slot));
		return true;
	}

	public ItemAttributeModifiers build() {
		return new ItemAttributeModifiers(List.copyOf(this.entries));
	}
}
