package twilightforest.port.neoforged.neoforge.event.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class EntityAttributeCreationEvent {
	public void put(EntityType<? extends LivingEntity> entityType, AttributeSupplier attributeSupplier) {
		FabricDefaultAttributeRegistry.register(entityType, attributeSupplier);
	}
}
