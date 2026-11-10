package twilightforest.port.neoforged.fml.event.lifecycle;

public class FMLCommonSetupEvent {
	public void enqueueWork(Runnable runnable) {
		if (runnable != null) {
			try {
				runnable.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
