package twilightforest.port.neoforged.neoforge.common;

import twilightforest.port.neoforged.bus.api.IEventBus;
import twilightforest.port.neoforged.bus.api.SimpleEventBus;

public final class NeoForge {
	public static final IEventBus EVENT_BUS = new SimpleEventBus();

	private NeoForge() {}
}
