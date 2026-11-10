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
 * Fabric port of a NeoForge enum extension: appends {@code TWILIGHTFOREST_PINCH} to the enum's values right after vanilla
 * builds them, before codecs and id maps are created from the values.
 */
@Mixin(net.minecraft.world.damagesource.DamageEffects.class)
public abstract class DamageEffectsExtensionMixin {
	@Shadow
	@Final
	@Mutable
	private static net.minecraft.world.damagesource.DamageEffects[] $VALUES;

	@Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/world/damagesource/DamageEffects;$VALUES:[Lnet/minecraft/world/damagesource/DamageEffects;", opcode = Opcodes.PUTSTATIC, shift = At.Shift.AFTER))
	private static void twilightforest$addConstant(CallbackInfo ci) {
		net.minecraft.world.damagesource.DamageEffects[] values = Arrays.copyOf($VALUES, $VALUES.length + 1);
		values[values.length - 1] = DamageEffectsInvoker.twilightforest$create("TWILIGHTFOREST_PINCH", values.length - 1, "twilightforest:pinch", net.minecraft.sounds.SoundEvent.createVariableRangeEvent(net.minecraft.resources.Identifier.fromNamespaceAndPath("twilightforest", "entity.twilightforest.pinch_beetle.attack")));
		$VALUES = values;
	}
}
