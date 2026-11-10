package twilightforest.port.neoforged.neoforge.client.event;

import twilightforest.port.neoforged.bus.api.Event;

public class CalculatePlayerTurnEvent extends Event {
	private boolean cinematicCameraEnabled;
	private double mouseSensitivity;

	public CalculatePlayerTurnEvent(boolean cinematicCameraEnabled, double mouseSensitivity) {
		this.cinematicCameraEnabled = cinematicCameraEnabled;
		this.mouseSensitivity = mouseSensitivity;
	}

	public boolean getCinematicCameraEnabled() {
		return cinematicCameraEnabled;
	}

	public double getMouseSensitivity() {
		return mouseSensitivity;
	}

	public void setMouseSensitivity(double mouseSensitivity) {
		this.mouseSensitivity = mouseSensitivity;
	}
}
