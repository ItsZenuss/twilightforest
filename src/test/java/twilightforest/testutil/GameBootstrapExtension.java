package twilightforest.testutil;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Auto-registered for every test class (see {@code junit-platform.properties}): starts Minecraft once per test run,
 * as NeoForge's JUnit runner did for the original suite. JUnit loads extensions with the application class loader,
 * while fabric-loader-junit loads the tests through Knot, so the bootstrap is delegated to a Knot-loaded class.
 */
public class GameBootstrapExtension implements BeforeAllCallback {
	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		ClassLoader knot = context.getRequiredTestClass().getClassLoader();
		Class.forName("twilightforest.testutil.GameBootstrap", true, knot).getMethod("start").invoke(null);
	}
}
