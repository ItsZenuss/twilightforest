package twilightforest.port.neoforged.neoforge.entity;

import net.minecraft.network.RegistryFriendlyByteBuf;

public interface IEntityWithComplexSpawn {
	void writeSpawnData(RegistryFriendlyByteBuf buffer);
	void readSpawnData(RegistryFriendlyByteBuf buf);
}
