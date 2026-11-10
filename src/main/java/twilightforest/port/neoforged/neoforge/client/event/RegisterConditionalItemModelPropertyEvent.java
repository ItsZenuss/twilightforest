package twilightforest.port.neoforged.neoforge.client.event;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import twilightforest.port.neoforged.bus.api.Event;

/**
 * Fabric port: registers straight into vanilla's id mapper.
 */
public class RegisterConditionalItemModelPropertyEvent extends Event {
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void register(Identifier id, MapCodec<?> codec) {
		((ExtraCodecs.LateBoundIdMapper) net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties.ID_MAPPER).put(id, codec);
	}
}
