package twilightforest.port.neoforged.bus.api;

public class Event {
	private boolean canceled = false;

	public boolean isCancelable() {
		return false;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean cancel) {
		this.canceled = cancel;
	}
}
