package twilightforest.mixin.compat;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(net.minecraft.world.damagesource.DamageEffects.class)
public interface DamageEffectsInvoker {
	@Invoker("<init>")
	static net.minecraft.world.damagesource.DamageEffects twilightforest$create(String internalName, int ordinal, String id, net.minecraft.sounds.SoundEvent sound) {
		throw new AssertionError();
	}
}
