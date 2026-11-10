package twilightforest.port.neoforged.neoforge.entity;

public interface IPartEntityHolder {
	default boolean isMultipartEntity() {
		return false;
	}

	default PartEntity<?>[] getParts() {
		return null;
	}
}
