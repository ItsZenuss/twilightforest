package twilightforest.port.neoforged.neoforge.client.model;

public record ExtraFaceData(int color, int blockLight, int skyLight, boolean ambientOcclusion) {
	public static final ExtraFaceData DEFAULT = new ExtraFaceData(-1, 0, 0, true);

	public ExtraFaceData(int color, int lightEmission, boolean ambientOcclusion) {
		this(color, lightEmission, 0, ambientOcclusion);
	}
}
