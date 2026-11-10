package twilightforest.mixin.event;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.entity.living.ArmorHurtEvent;
import org.spongepowered.asm.mixin.Mixin;

/** Fabric port: fires ArmorHurtEvent before equipment takes damage from an attack. */
@Mixin(LivingEntity.class)
public abstract class LivingEntityArmorEventMixin {
	@WrapMethod(method = "doHurtEquipment")
	private void tf$armorHurt(DamageSource source, float amount, EquipmentSlot[] slots, Operation<Void> original) {
		LivingEntity self = (LivingEntity) (Object) this;
		ArmorHurtEvent event = new ArmorHurtEvent(self);
		for (EquipmentSlot slot : slots) {
			if (!self.getItemBySlot(slot).isEmpty()) event.setNewDamage(slot, amount);
		}
		if (event.getArmorMap().isEmpty()) {
			original.call(source, amount, slots);
			return;
		}
		if (NeoForge.EVENT_BUS.post(event).isCanceled()) return;
		for (EquipmentSlot slot : slots) {
			float damage = event.getNewDamage(slot);
			if (damage > 0) original.call(source, damage, new EquipmentSlot[]{slot});
		}
	}
}
