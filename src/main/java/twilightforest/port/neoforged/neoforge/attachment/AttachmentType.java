package twilightforest.port.neoforged.neoforge.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

/**
 * Fabric port of NeoForge's AttachmentType, backed by Fabric's data attachment API. The Fabric attachment is
 * created when the type is registered through a {@link twilightforest.port.neoforged.neoforge.registries.DeferredRegister}.
 */
public class AttachmentType<T> {
	private final Supplier<T> defaultValueSupplier;
	private final boolean copyOnDeath;
	@org.jetbrains.annotations.Nullable
	private final Codec<T> codec;
	@org.jetbrains.annotations.Nullable
	private final StreamCodec<? super net.minecraft.network.RegistryFriendlyByteBuf, T> streamCodec;
	private net.fabricmc.fabric.api.attachment.v1.AttachmentType<T> fabricAttachment;

	public AttachmentType(Supplier<T> defaultValueSupplier, boolean copyOnDeath) {
		this(defaultValueSupplier, copyOnDeath, null, null);
	}

	private AttachmentType(Supplier<T> defaultValueSupplier, boolean copyOnDeath, @org.jetbrains.annotations.Nullable Codec<T> codec, @org.jetbrains.annotations.Nullable StreamCodec<? super net.minecraft.network.RegistryFriendlyByteBuf, T> streamCodec) {
		this.defaultValueSupplier = defaultValueSupplier;
		this.copyOnDeath = copyOnDeath;
		this.codec = codec;
		this.streamCodec = streamCodec;
	}

	public Supplier<T> getDefaultValueSupplier() {
		return defaultValueSupplier;
	}

	public boolean isCopyOnDeath() {
		return copyOnDeath;
	}

	public net.fabricmc.fabric.api.attachment.v1.AttachmentType<T> getFabricAttachment() {
		return fabricAttachment;
	}

	public void registerFabric(Identifier id) {
		if (this.fabricAttachment != null) return;
		AttachmentRegistry.Builder<T> builder = AttachmentRegistry.builder();
		if (defaultValueSupplier != null) {
			builder.initializer(defaultValueSupplier);
		}
		if (codec != null) {
			builder.persistent(codec);
		}
		if (streamCodec != null) {
			builder.syncWith(streamCodec, net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate.all());
		}
		if (copyOnDeath) {
			builder.copyOnDeath();
		}
		this.fabricAttachment = builder.buildAndRegister(id);
	}

	public static <T> Builder<T> builder(Supplier<T> defaultValueSupplier) {
		return new Builder<>(defaultValueSupplier);
	}

	public static class Builder<T> {
		private final Supplier<T> defaultValueSupplier;
		private boolean copyOnDeath = false;
		private Codec<T> codec;
		private StreamCodec<? super net.minecraft.network.RegistryFriendlyByteBuf, T> streamCodec;

		public Builder(Supplier<T> defaultValueSupplier) {
			this.defaultValueSupplier = defaultValueSupplier;
		}

		public Builder<T> serialize(Codec<T> codec) {
			this.codec = codec;
			return this;
		}

		public Builder<T> serialize(MapCodec<T> mapCodec) {
			this.codec = mapCodec.codec();
			return this;
		}

		@SuppressWarnings("unchecked")
		public <B> Builder<T> sync(StreamCodec<B, T> streamCodec) {
			this.streamCodec = (StreamCodec<? super net.minecraft.network.RegistryFriendlyByteBuf, T>) (StreamCodec<?, T>) streamCodec;
			return this;
		}

		public Builder<T> copyOnDeath() {
			this.copyOnDeath = true;
			return this;
		}

		public AttachmentType<T> build() {
			return new AttachmentType<>(defaultValueSupplier, copyOnDeath, codec, streamCodec);
		}
	}
}
