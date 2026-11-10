package twilightforest.port.neoforged.neoforge.registries.datamaps;

import net.minecraft.core.Holder;

public interface IHolderDataMapExtension {
	@SuppressWarnings("unchecked")
	default <T> T getData(DataMapType<?, T> type) {
		if (this instanceof Holder holder) {
			return (T) type.get(holder);
		}
		return null;
	}
}
