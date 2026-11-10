package twilightforest.mixin.compat;


import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

/**
 * Fabric port of a NeoForge enum extension: appends {@code TWILIGHTFOREST_JARRED} to the enum's values right after vanilla
 * builds them, before codecs and id maps are created from the values.
 */
@Mixin(net.minecraft.world.item.ItemDisplayContext.class)
public abstract class ItemDisplayContextExtensionMixin {
	@Shadow
	@Final
	@Mutable
	private static net.minecraft.world.item.ItemDisplayContext[] $VALUES;

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/item/ItemDisplayContext;$VALUES:[Lnet/minecraft/world/item/ItemDisplayContext;", opcode = Opcodes.PUTSTATIC, shift = At.Shift.AFTER))
	private static void twilightforest$addConstant(CallbackInfo ci) {
		net.minecraft.world.item.ItemDisplayContext[] values = Arrays.copyOf($VALUES, $VALUES.length + 1);
		values[values.length - 1] = ItemDisplayContextInvoker.twilightforest$create("TWILIGHTFOREST_JARRED", values.length - 1, values.length - 1, "twilightforest:jarred");
		$VALUES = values;
	}
}
