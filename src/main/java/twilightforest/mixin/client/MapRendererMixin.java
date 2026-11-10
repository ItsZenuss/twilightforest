package twilightforest.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MapRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import twilightforest.port.neoforged.neoforge.client.gui.map.IMapDecorationRenderer;
import twilightforest.port.neoforged.neoforge.client.gui.map.RegisterMapDecorationRenderersEvent;
import twilightforest.port.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fabric port of NeoForge's map rendering hooks: decoration types and render-state modifiers at extraction, and
 * custom decoration renderers (which may replace the vanilla icon) when drawing.
 */
@Mixin(MapRenderer.class)
public abstract class MapRendererMixin {
	@Shadow
	@Final
	private TextureAtlas decorationSprites;

	@Unique
	private int twilightforest$index;
	@Unique
	private boolean twilightforest$skipIcon;

	@ModifyReturnValue(method = "extractDecorationRenderState", at = @At("RETURN"))
	private MapRenderState.MapDecorationRenderState twilightforest$recordType(MapRenderState.MapDecorationRenderState state, MapDecoration decoration) {
		state.setType(decoration.type());
		return state;
	}

	@Inject(method = "extractRenderState", at = @At("TAIL"))
	private void twilightforest$modifiers(MapId id, MapItemSavedData data, MapRenderState state, CallbackInfo ci) {
		RegisterRenderStateModifiersEvent.applyMapModifiers(data, state);
	}

	@Inject(method = "render", at = @At("HEAD"))
	private void twilightforest$resetIndex(MapRenderState state, PoseStack stack, SubmitNodeCollector collector, boolean inFrame, int light, CallbackInfo ci) {
		this.twilightforest$index = 0;
	}

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", ordinal = 0))
	private void twilightforest$customRenderer(MapRenderState state, PoseStack stack, SubmitNodeCollector collector, boolean inFrame, int light, CallbackInfo ci,
											   @Local MapRenderState.MapDecorationRenderState decoration) {
		this.twilightforest$skipIcon = false;
		if (decoration.getType() != null) {
			IMapDecorationRenderer renderer = RegisterMapDecorationRenderersEvent.get(decoration.getType().value());
			if (renderer != null) {
				this.twilightforest$skipIcon = renderer.render(decoration, stack, collector, state, this.decorationSprites, inFrame, light, this.twilightforest$index);
			}
		}
		this.twilightforest$index++;
	}

	@ModifyExpressionValue(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/state/MapRenderState$MapDecorationRenderState;atlasSprite:Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"))
	private TextureAtlasSprite twilightforest$skipVanillaIcon(TextureAtlasSprite sprite) {
		return this.twilightforest$skipIcon ? null : sprite;
	}
}
