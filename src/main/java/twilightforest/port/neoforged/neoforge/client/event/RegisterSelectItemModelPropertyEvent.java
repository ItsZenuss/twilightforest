package twilightforest.port.neoforged.neoforge.client.event;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.Identifier;
import twilightforest.port.neoforged.bus.api.Event;

public class RegisterSelectItemModelPropertyEvent extends Event {
	public void register(Identifier id, MapCodec<?> codec) {
		throw new UnsupportedOperationException("Select item model properties are registered by Type");
	}

	public void register(Identifier id, SelectItemModelProperty.Type<?, ?> type) {
		SelectItemModelProperties.ID_MAPPER.put(id, type);
	}
}
