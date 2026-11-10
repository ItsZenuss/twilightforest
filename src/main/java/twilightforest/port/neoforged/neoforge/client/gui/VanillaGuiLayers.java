package twilightforest.port.neoforged.neoforge.client.gui;

import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.resources.Identifier;

/**
 * Fabric port: NeoForge's vanilla GUI layer ids, mapped onto Fabric's vanilla HUD element ids.
 */
public final class VanillaGuiLayers {
	private VanillaGuiLayers() {}

	public static final Identifier VEHICLE_HEALTH = VanillaHudElements.MOUNT_HEALTH;
	public static final Identifier CROSSHAIR = VanillaHudElements.CROSSHAIR;
	public static final Identifier ARMOR_LEVEL = VanillaHudElements.ARMOR_BAR;
	public static final Identifier PLAYER_HEALTH = VanillaHudElements.HEALTH_BAR;
	public static final Identifier FOOD_LEVEL = VanillaHudElements.FOOD_BAR;
	public static final Identifier AIR_LEVEL = VanillaHudElements.AIR_BAR;
	public static final Identifier EXPERIENCE_BAR = VanillaHudElements.INFO_BAR;
	public static final Identifier HOTBAR = VanillaHudElements.HOTBAR;
}
