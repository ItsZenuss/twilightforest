package twilightforest.port.neoforged.bus.api;

public interface ICancellableEvent {
	boolean isCanceled();
	void setCanceled(boolean cancel);
}
