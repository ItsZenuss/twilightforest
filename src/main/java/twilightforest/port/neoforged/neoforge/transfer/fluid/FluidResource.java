package twilightforest.port.neoforged.neoforge.transfer.fluid;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FluidResource {
	public static final FluidResource EMPTY = new FluidResource(Fluids.EMPTY);

	private final Fluid fluid;
	private final DataComponentPatch patch;

	public FluidResource(Fluid fluid) {
		this(fluid, DataComponentPatch.EMPTY);
	}

	public FluidResource(Fluid fluid, DataComponentPatch patch) {
		this.fluid = fluid;
		this.patch = patch != null ? patch : DataComponentPatch.EMPTY;
	}

	public Fluid getFluid() {
		return fluid;
	}

	public boolean is(Fluid other) {
		return fluid == other;
	}

	public boolean isEmpty() {
		return fluid == Fluids.EMPTY;
	}

	public DataComponentPatch getComponentsPatch() {
		return patch;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof FluidResource other && other.fluid == this.fluid && other.patch.equals(this.patch);
	}

	@Override
	public int hashCode() {
		return 31 * this.fluid.hashCode() + this.patch.hashCode();
	}
}
