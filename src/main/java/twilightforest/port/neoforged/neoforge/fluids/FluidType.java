package twilightforest.port.neoforged.neoforge.fluids;

public class FluidType {
	public static final int BUCKET_VOLUME = 1000;
	public static final FluidType WATER = new FluidType("water", net.minecraft.tags.FluidTags.WATER);
	public static final FluidType LAVA = new FluidType("lava", net.minecraft.tags.FluidTags.LAVA);

	private final String name;
	private final net.minecraft.tags.TagKey<net.minecraft.world.level.material.Fluid> tag;

	public FluidType(String name) {
		this(name, net.minecraft.tags.FluidTags.WATER);
	}

	/**
	 * Fabric port: vanilla has no fluid types, so each type maps onto the fluid tag vanilla uses for its physics.
	 */
	public FluidType(String name, net.minecraft.tags.TagKey<net.minecraft.world.level.material.Fluid> tag) {
		this.name = name;
		this.tag = tag;
	}

	public net.minecraft.tags.TagKey<net.minecraft.world.level.material.Fluid> getTag() {
		return tag;
	}

	public String getName() {
		return name;
	}
}
