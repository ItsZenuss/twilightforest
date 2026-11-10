package tamaized.beanification.junit;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;

/**
 * Fabric port of Beanification's JUnit helper. On NeoForge it re-pointed Mockito at the mod class loader; here it
 * only clears inline mocks between test classes so state from one class does not leak into the next.
 */
public class MockitoFixer implements BeforeAllCallback {
	@Override
	public void beforeAll(ExtensionContext context) {
		Mockito.framework().clearInlineMocks();
	}
}
