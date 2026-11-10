package twilightforest.port.neoforged.neoforge.common.util;

public enum TriState {
	TRUE(true),
	FALSE(false),
	DEFAULT(false);

	private final boolean boolValue;

	TriState(boolean boolValue) {
		this.boolValue = boolValue;
	}

	public boolean getAsBoolean() {
		return boolValue;
	}

	public boolean isTrue() {
		return this == TRUE;
	}

	public boolean isFalse() {
		return this == FALSE;
	}

	public static TriState of(boolean value) {
		return value ? TRUE : FALSE;
	}

	public boolean isDefault() {
		return this == DEFAULT;
	}
}
