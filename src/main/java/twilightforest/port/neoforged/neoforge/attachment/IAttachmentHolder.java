package twilightforest.port.neoforged.neoforge.attachment;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import java.util.function.Supplier;

public interface IAttachmentHolder {
	default <T> boolean hasData(AttachmentType<T> type) {
		if (this instanceof AttachmentTarget target && type.getFabricAttachment() != null) {
			return target.hasAttached(type.getFabricAttachment());
		}
		return false;
	}

	default <T> boolean hasData(Supplier<? extends AttachmentType<T>> type) {
		return hasData(type.get());
	}

	@SuppressWarnings("unchecked")
	default <T> T getData(AttachmentType<T> type) {
		if (this instanceof AttachmentTarget target && type.getFabricAttachment() != null) {
			return target.getAttachedOrCreate(type.getFabricAttachment());
		}
		return type.getDefaultValueSupplier() != null ? type.getDefaultValueSupplier().get() : null;
	}

	default <T> T getData(Supplier<? extends AttachmentType<T>> type) {
		return getData(type.get());
	}

	default <T> T setData(AttachmentType<T> type, T value) {
		if (this instanceof AttachmentTarget target && type.getFabricAttachment() != null) {
			return target.setAttached(type.getFabricAttachment(), value);
		}
		return value;
	}

	default <T> T setData(Supplier<? extends AttachmentType<T>> type, T value) {
		return setData(type.get(), value);
	}

	default <T> T removeData(AttachmentType<T> type) {
		if (this instanceof AttachmentTarget target && type.getFabricAttachment() != null) {
			return target.removeAttached(type.getFabricAttachment());
		}
		return null;
	}

	default <T> T removeData(Supplier<? extends AttachmentType<T>> type) {
		return removeData(type.get());
	}

	/**
	 * Implemented per entity (and saved) by {@code twilightforest.mixin.compat.EntityExtensionMixin}.
	 */
	default net.minecraft.nbt.CompoundTag getPersistentData() {
		throw new UnsupportedOperationException("getPersistentData is only available on entities");
	}
}
