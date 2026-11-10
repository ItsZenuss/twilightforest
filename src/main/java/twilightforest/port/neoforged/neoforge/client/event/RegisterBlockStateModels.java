package twilightforest.port.neoforged.neoforge.client.event;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.client.model.loading.v1.CustomUnbakedBlockStateModel;
import net.minecraft.resources.Identifier;
import twilightforest.port.neoforged.bus.api.Event;

public class RegisterBlockStateModels extends Event {
	@SuppressWarnings("unchecked")
	public void registerModel(Identifier id, MapCodec<?> codec) {
		CustomUnbakedBlockStateModel.register(id, (MapCodec<? extends CustomUnbakedBlockStateModel>) codec);
	}
}
