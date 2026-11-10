package twilightforest.port.neoforged.neoforge.registries.datamaps;

import java.util.ArrayList;
import java.util.List;

public class RegisterDataMapTypesEvent {
	private final List<DataMapType<?, ?>> registered = new ArrayList<>();

	public void register(DataMapType<?, ?> type) {
		registered.add(type);
	}

	public List<DataMapType<?, ?>> getRegistered() {
		return registered;
	}
}
