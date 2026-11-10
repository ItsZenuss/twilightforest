package twilightforest.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import tamaized.beanification.BeanContext;
import twilightforest.client.GrassColorHandler;

/**
 * Fabric port: applies Twilight Forest's biome grass colour algorithms (NeoForge registered these as custom
 * GrassColorModifier enum values, which Fabric can't add).
 */
@Mixin(Biome.class)
public abstract class BiomeGrassColorMixin {
	@Unique
	private static GrassColorHandler tf$handler;

	@ModifyReturnValue(method = "getGrassColor", at = @At("RETURN"))
	private int tf$twilightGrass(int original, double x, double z) {
		if (tf$handler == null) tf$handler = BeanContext.get(GrassColorHandler.class);
		return tf$handler == null ? original : tf$handler.get(original, (Biome) (Object) this, x, z);
	}
}
