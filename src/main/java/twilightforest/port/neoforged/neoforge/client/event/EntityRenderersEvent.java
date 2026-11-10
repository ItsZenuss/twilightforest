package twilightforest.port.neoforged.neoforge.client.event;

import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import twilightforest.port.neoforged.bus.api.Event;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Fabric port of NeoForge's EntityRenderersEvent family, backed by Fabric's rendering API.
 */
public class EntityRenderersEvent extends Event {
	public static class RegisterRenderers extends EntityRenderersEvent {
		public <T extends Entity> void registerEntityRenderer(EntityType<? extends T> type, EntityRendererProvider<T> provider) {
			EntityRendererRegistry.register(type, provider);
		}

		@SuppressWarnings({"rawtypes", "unchecked"})
		public void registerBlockEntityRenderer(BlockEntityType<?> type, BlockEntityRendererProvider provider) {
			BlockEntityRendererRegistry.register((BlockEntityType<BlockEntity>) type, provider);
		}
	}

	public static class RegisterLayerDefinitions extends EntityRenderersEvent {
		public void registerLayerDefinition(ModelLayerLocation layer, Supplier<LayerDefinition> supplier) {
			ModelLayerRegistry.registerModelLayer(layer, supplier::get);
		}
	}

	/**
	 * Fired once per living-entity renderer from Fabric's LivingEntityRenderLayerRegistrationCallback, so it only
	 * exposes that one renderer (as an entity type renderer, or as a player skin renderer for players).
	 */
	public static class AddLayers extends EntityRenderersEvent {
		private final EntityType<?> type;
		private final EntityRenderer<?, ?> renderer;
		private final EntityRendererProvider.Context context;

		public AddLayers(EntityType<?> type, EntityRenderer<?, ?> renderer, EntityRendererProvider.Context context) {
			this.type = type;
			this.renderer = renderer;
			this.context = context;
		}

		private boolean isPlayer() {
			return this.renderer instanceof AvatarRenderer<?>;
		}

		public EntityRendererProvider.Context getContext() {
			return this.context;
		}

		public Collection<EntityType<?>> getEntityTypes() {
			return this.isPlayer() ? List.of() : List.of(this.type);
		}

		public EntityRenderer<?, ?> getRenderer(EntityType<?> type) {
			return type == this.type ? this.renderer : null;
		}

		public Set<PlayerModelType> getSkins() {
			// The callback doesn't say which skin model this is; any key maps back to this renderer.
			return this.isPlayer() ? Set.of(PlayerModelType.WIDE) : Set.of();
		}

		@SuppressWarnings("unchecked")
		public AvatarRenderer<AbstractClientPlayer> getPlayerRenderer(PlayerModelType model) {
			return this.isPlayer() ? (AvatarRenderer<AbstractClientPlayer>) this.renderer : null;
		}
	}
}
