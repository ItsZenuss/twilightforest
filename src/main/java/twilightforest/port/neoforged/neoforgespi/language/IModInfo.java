package twilightforest.port.neoforged.neoforgespi.language;

public interface IModInfo {
	String getDisplayName();
	default String getModId() {
		return "";
	}
}
