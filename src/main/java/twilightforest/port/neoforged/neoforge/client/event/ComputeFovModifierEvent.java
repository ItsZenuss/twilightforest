package twilightforest.port.neoforged.neoforge.client.event;

import net.minecraft.world.entity.player.Player;
import twilightforest.port.neoforged.bus.api.Event;

public class ComputeFovModifierEvent extends Event {
	private final Player player;
	private final float fovModifier;
	private float newFovModifier;

	public ComputeFovModifierEvent(Player player, float fovModifier) {
		this.player = player;
		this.fovModifier = fovModifier;
		this.newFovModifier = fovModifier;
	}

	public Player getPlayer() {
		return player;
	}

	public float getFovModifier() {
		return fovModifier;
	}

	public float getNewFovModifier() {
		return newFovModifier;
	}

	public void setNewFovModifier(float newFovModifier) {
		this.newFovModifier = newFovModifier;
	}

	public float getFovScale() {
		return 1.0F;
	}
}
