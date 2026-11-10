package twilightforest.port.neoforged.neoforge.registries;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public class DeferredDataComponent<T> extends DeferredHolder<DataComponentType<?>, DataComponentType<T>> implements DataComponentType<T> {

	public DeferredDataComponent(ResourceKey<DataComponentType<?>> key, Supplier<? extends DataComponentType<T>> supplier) {
		super(key, supplier);
	}

	@Override
	public Codec<T> codec() {
		return get().codec();
	}

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
		return get().streamCodec();
	}

	@Override
	public boolean ignoreSwapAnimation() {
		return get().ignoreSwapAnimation();
	}
}
