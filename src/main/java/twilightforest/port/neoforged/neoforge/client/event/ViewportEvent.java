package twilightforest.port.neoforged.neoforge.client.event;

import net.minecraft.client.Camera;
import twilightforest.port.neoforged.bus.api.Event;

public class ViewportEvent extends Event {
	private final Camera camera;

	public ViewportEvent(Camera camera) {
		this.camera = camera;
	}

	public Camera getCamera() {
		return camera;
	}

	public static class ComputeFogColor extends ViewportEvent {
		private float red, green, blue;

		public ComputeFogColor(Camera camera, float r, float g, float b) {
			super(camera);
			this.red = r;
			this.green = g;
			this.blue = b;
		}

		public float getRed() { return red; }
		public float getGreen() { return green; }
		public float getBlue() { return blue; }
		public void setRed(float red) { this.red = red; }
		public void setGreen(float green) { this.green = green; }
		public void setBlue(float blue) { this.blue = blue; }
	}

	public static class ComputeCameraAngles extends ViewportEvent {
		private float yaw, pitch, roll, partialTick;

		public ComputeCameraAngles(Camera camera, float yaw, float pitch, float roll, float partialTick) {
			super(camera);
			this.yaw = yaw;
			this.pitch = pitch;
			this.roll = roll;
			this.partialTick = partialTick;
		}

		public float getYaw() { return yaw; }
		public void setYaw(float yaw) { this.yaw = yaw; }
		public float getPitch() { return pitch; }
		public void setPitch(float pitch) { this.pitch = pitch; }
		public float getRoll() { return roll; }
		public void setRoll(float roll) { this.roll = roll; }
		public float getPartialTick() { return partialTick; }
	}
}
