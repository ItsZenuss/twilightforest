package twilightforest.mixin.compat;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import twilightforest.port.neoforged.neoforge.attachment.IAttachmentHolder;
import twilightforest.port.neoforged.neoforge.common.extensions.IEntityExtension;
import twilightforest.port.neoforged.neoforge.entity.IPartEntityHolder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fabric port: implements NeoForge's entity extension interfaces at runtime and gives each entity its own
 * persistent data tag, saved under "NeoForgeData" like on NeoForge so existing worlds keep their data.
 */
@Mixin(Entity.class)
public abstract class EntityExtensionMixin implements IAttachmentHolder, IPartEntityHolder, IEntityExtension {
	@Unique
	private static final String TF_PERSISTENT_KEY = "NeoForgeData";

	@Unique
	@Nullable
	private CompoundTag tf$persistentData;

	@Override
	public CompoundTag getPersistentData() {
		if (this.tf$persistentData == null) this.tf$persistentData = new CompoundTag();
		return this.tf$persistentData;
	}

	@Inject(method = "saveWithoutId", at = @At("TAIL"))
	private void tf$savePersistentData(ValueOutput output, CallbackInfo ci) {
		if (this.tf$persistentData != null && !this.tf$persistentData.isEmpty()) {
			output.store(TF_PERSISTENT_KEY, CompoundTag.CODEC, this.tf$persistentData);
		}
	}

	@Inject(method = "load", at = @At("TAIL"))
	private void tf$loadPersistentData(ValueInput input, CallbackInfo ci) {
		input.read(TF_PERSISTENT_KEY, CompoundTag.CODEC).ifPresent(tag -> this.tf$persistentData = tag);
	}
}
