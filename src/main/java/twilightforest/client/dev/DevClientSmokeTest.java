package twilightforest.client.dev;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFDimension;

/**
 * Development-only, enabled with {@code -Dtwilightforest.smoketest.screenshots=true} (set by the "clientTest" run
 * config): once the player is in the Twilight Forest, saves a few screenshots so rendering can be checked without
 * interacting with the window. Does nothing in normal play.
 */
public final class DevClientSmokeTest {
	private static int ticksInForest;
	private static int shots;

	private DevClientSmokeTest() {}

	public static void init() {
		if (!Boolean.getBoolean("twilightforest.smoketest.screenshots")) return;
		ClientTickEvents.END_CLIENT_TICK.register(DevClientSmokeTest::tick);
	}

	private static void tick(Minecraft mc) {
		if (mc.level == null || mc.player == null || !mc.level.dimension().equals(TFDimension.DIMENSION_KEY) || shots >= 3) return;
		mc.options.pauseOnLostFocus = false;
		if (mc.screen instanceof net.minecraft.client.gui.screens.PauseScreen) mc.setScreen(null);
		ticksInForest++;
		// last shot from the front (switched a few seconds early so it has rendered), so worn trinkets are visible
		if (shots == 2 && ticksInForest % 200 == 100 && System.getProperty("twilightforest.smoketest.focus") == null) mc.options.setCameraType(net.minecraft.client.CameraType.THIRD_PERSON_FRONT);
		if (ticksInForest % 200 == 0) {
			if (shots < 2 && System.getProperty("twilightforest.smoketest.focus") == null) mc.player.setYRot(mc.player.getYRot() + 120.0F);
			Screenshot.grab(mc.gameDirectory, "tf_smoketest_" + shots + ".png", mc.getMainRenderTarget(), 1,
				msg -> TwilightForestMod.LOGGER.info("[smoketest] {}", msg.getString()));
			shots++;
		}
	}
}
