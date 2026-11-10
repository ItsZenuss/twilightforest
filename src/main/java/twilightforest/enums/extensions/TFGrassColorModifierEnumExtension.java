package twilightforest.enums.extensions;

import net.minecraft.world.level.biome.BiomeSpecialEffects;
import tamaized.beanification.Component;

/**
 * Fabric port: NeoForge enum extensions are unavailable. Twilight biomes use {@code NONE} in data and
 * their custom grass colours are applied client-side by {@link twilightforest.client.GrassColorHandler}.
 */
@Component
public class TFGrassColorModifierEnumExtension {

	public final BiomeSpecialEffects.GrassColorModifier ENCHANTED_FOREST = BiomeSpecialEffects.GrassColorModifier.NONE;
	public final BiomeSpecialEffects.GrassColorModifier SWAMP = BiomeSpecialEffects.GrassColorModifier.NONE;
	public final BiomeSpecialEffects.GrassColorModifier DARK_FOREST = BiomeSpecialEffects.GrassColorModifier.NONE;
	public final BiomeSpecialEffects.GrassColorModifier DARK_FOREST_CENTER = BiomeSpecialEffects.GrassColorModifier.NONE;
	public final BiomeSpecialEffects.GrassColorModifier SPOOKY_FOREST = BiomeSpecialEffects.GrassColorModifier.NONE;

}
