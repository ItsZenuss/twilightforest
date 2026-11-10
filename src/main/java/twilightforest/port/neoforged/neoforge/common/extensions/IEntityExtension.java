package twilightforest.port.neoforged.neoforge.common.extensions;

import twilightforest.port.neoforged.neoforge.fluids.FluidType;

public interface IEntityExtension {
	default boolean canRiderInteract() {
		return false;
	}

	default boolean isAffectedByFluids() {
		return true;
	}

	default boolean isPushedByFluid(FluidType type) {
		return true;
	}

	default double getFluidTypeHeight(FluidType type) {
		return ((net.minecraft.world.entity.Entity) this).getFluidHeight(type.getTag());
	}

	default void sendPairingData(net.minecraft.server.level.ServerPlayer player, java.util.function.Consumer<net.minecraft.network.protocol.common.custom.CustomPacketPayload> bundleBuilder) {
	}
}
