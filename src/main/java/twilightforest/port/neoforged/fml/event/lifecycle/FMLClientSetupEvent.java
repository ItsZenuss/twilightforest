package twilightforest.port.neoforged.fml.event.lifecycle;

import twilightforest.port.neoforged.bus.api.Event;

public class FMLClientSetupEvent extends Event {
	public void enqueueWork(Runnable runnable) {
		runnable.run();
	}
}
