package twilightforest.port.neoforged.neoforge.client.event;

import net.minecraft.client.player.ClientInput;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.port.neoforged.bus.api.Event;

public class MovementInputUpdateEvent extends Event {
	private final LivingEntity entity;
	private final ClientInput input;

	public MovementInputUpdateEvent(LivingEntity entity, ClientInput input) {
		this.entity = entity;
		this.input = input;
	}

	public LivingEntity getEntity() {
		return entity;
	}

	public ClientInput getInput() {
		return input;
	}
}
