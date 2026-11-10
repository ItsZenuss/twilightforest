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
 * Fabric port of a NeoForge enum extension: appends {@code TWILIGHTFOREST_TWILIGHT} to the enum's values right after vanilla
 * builds them, before codecs and id maps are created from the values.
 */
@Mixin(net.minecraft.world.item.Rarity.class)
public abstract class RarityExtensionMixin {
	@Shadow
	@Final
	@Mutable
	private static net.minecraft.world.item.Rarity[] $VALUES;

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/item/Rarity;$VALUES:[Lnet/minecraft/world/item/Rarity;", opcode = Opcodes.PUTSTATIC, shift = At.Shift.AFTER))
	private static void twilightforest$addConstant(CallbackInfo ci) {
		net.minecraft.world.item.Rarity[] values = Arrays.copyOf($VALUES, $VALUES.length + 1);
		values[values.length - 1] = RarityInvoker.twilightforest$create("TWILIGHTFOREST_TWILIGHT", values.length - 1, values.length - 1, "twilightforest:twilight", net.minecraft.ChatFormatting.DARK_GREEN);
		$VALUES = values;
	}
}
