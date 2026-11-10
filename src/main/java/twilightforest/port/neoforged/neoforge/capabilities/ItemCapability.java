package twilightforest.port.neoforged.neoforge.capabilities;

public class ItemCapability<R, C> {
	private final String name;

	public ItemCapability(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
