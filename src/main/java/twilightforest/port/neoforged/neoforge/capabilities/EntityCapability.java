package twilightforest.port.neoforged.neoforge.capabilities;

public class EntityCapability<R, C> {
	private final String name;

	public EntityCapability(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
