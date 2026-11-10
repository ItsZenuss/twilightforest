package twilightforest.port.neoforged.neoforge.event.entity.living;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import twilightforest.port.neoforged.bus.api.ICancellableEvent;
import twilightforest.port.neoforged.neoforge.event.entity.EntityEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ArmorHurtEvent extends EntityEvent implements ICancellableEvent {
	private final LivingEntity entity;
	private final Map<EquipmentSlot, Float> armorMap = new HashMap<>();
	private boolean canceled;

	public ArmorHurtEvent(LivingEntity entity) {
		super(entity);
		this.entity = entity;
	}

	@Override
	public LivingEntity getEntity() {
		return entity;
	}

	public Map<EquipmentSlot, Float> getArmorMap() {
		return armorMap;
	}

	public ItemStack getArmorItemStack(EquipmentSlot slot) {
		return entity.getItemBySlot(slot);
	}

	public float getNewDamage(EquipmentSlot slot) {
		return armorMap.getOrDefault(slot, 0.0f);
	}

	public void setNewDamage(EquipmentSlot slot, float damage) {
		armorMap.put(slot, damage);
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
