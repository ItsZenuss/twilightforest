package twilightforest;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import twilightforest.port.neoforged.fml.ModLoadingContext;
import twilightforest.port.neoforged.fml.config.ModConfig;
import twilightforest.port.neoforged.fml.event.config.ModConfigEvent;
import twilightforest.port.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import twilightforest.port.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import twilightforest.port.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import twilightforest.port.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import twilightforest.port.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import twilightforest.port.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import twilightforest.port.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import twilightforest.port.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import twilightforest.port.neoforged.neoforge.registries.DataPackRegistryEvent;
import twilightforest.port.neoforged.neoforge.registries.NewRegistryEvent;
import twilightforest.port.neoforged.neoforge.registries.RegisterEvent;
import twilightforest.port.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import twilightforest.port.neoforged.neoforge.server.ServerLifecycleHooks;
import twilightforest.fabric.NeoForgeEventBridge;
import twilightforest.fabric.TFDataMapLoader;
import java.nio.file.Path;
import java.util.List;
import twilightforest.port.neoforged.neoforge.registries.DeferredRegister;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import twilightforest.port.neoforged.api.distmarker.Dist;
import twilightforest.port.neoforged.bus.api.IEventBus;
import twilightforest.port.neoforged.bus.api.SimpleEventBus;
import twilightforest.port.neoforged.fml.ModList;
import twilightforest.port.neoforged.fml.loading.FMLEnvironment;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tamaized.beanification.BeanContext;
import tamaized.beanification.Configurable;
import twilightforest.config.ConfigSetup;
import twilightforest.init.*;
import twilightforest.init.custom.*;
import twilightforest.util.TFRemapper;

import java.util.Locale;

@Configurable
public final class TwilightForestMod implements ModInitializer {

	public static final String ID = "twilightforest";

	private static final String MODEL_DIR = "textures/entity/";
	private static final String GUI_DIR = "textures/gui/";
	private static final String ENVIRO_DIR = "textures/environment/";

	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	public static final IEventBus MOD_BUS = new SimpleEventBus();

	static {
		BeanContext.init(ID);
	}

	@Override
	public void onInitialize() {
		ServerLifecycleHooks.init();
		BeanContext.setModBus(MOD_BUS);

		// Custom static registries must exist before anything registers into them.
		Reflection.initialize(TFRegistries.class);

		// Construct every @Component so their listeners are attached before any lifecycle event fires.
		BeanContext.start();

		this.loadConfigs();

		MOD_BUS.post(new NewRegistryEvent());
		MOD_BUS.post(new DataPackRegistryEvent.NewRegistry());

		// Register all deferred registries (blocks before items, like NeoForge)
		registerInPriorityOrder(List.of(
			TFBlocks.BLOCKS,
			TFItems.ITEMS,
			TFEntities.SPAWN_EGGS,
			TFStats.STATS,
			TFLoot.NUMBERS,
			TFPOITypes.POIS,
			TFSounds.SOUNDS,
			TFLoot.FUNCTIONS,
			TFLoot.CONDITIONS,
			TFGameRules.RULES,
			TFFeatures.FEATURES,
			TFCreativeTabs.TABS,
			TFLoot.CONDITIONALS,
			ItemDisplays.DISPLAYS,
			TFMenuTypes.CONTAINERS,
			TFRecipes.RECIPE_TYPES,
			TFEntities.ENTITY_TYPES,
			TFAttributes.ATTRIBUTES,
			TFAdvancements.TRIGGERS,
			TFMobEffects.MOB_EFFECTS,
			TFItemSubPredicates.TYPES,
			Enforcements.ENFORCEMENTS,
			TFCaveCarvers.CARVER_TYPES,
			TFDataComponents.COMPONENTS,
			TFRecipes.RECIPE_SERIALIZERS,
			TFMapDecorations.DECORATIONS,
			TFParticleType.PARTICLE_TYPES,
			TravellersModifierTypes.TYPES,
			TFBlockEntities.BLOCK_ENTITIES,
			TFLootModifiers.LOOT_MODIFIERS,
			TFConsumeEffects.CONSUME_EFFECTS,
			TFStructureTypes.STRUCTURE_TYPES,
			TFFeatureModifiers.TRUNK_PLACERS,
			BiomeLayerTypes.BIOME_LAYER_TYPES,
			TFDataAttachments.ATTACHMENT_TYPES,
			TFDataSerializers.DATA_SERIALIZERS,
			TFFeatureModifiers.FOLIAGE_PLACERS,
			TFFeatureModifiers.TREE_DECORATORS,
			TFEnchantmentEffects.ENTITY_EFFECTS,
			TFFeatureModifiers.PLACEMENT_MODIFIERS,
			TFDensityFunctions.DENSITY_FUNCTION_TYPES,
			TFStructureProcessors.STRUCTURE_PROCESSORS,
			TFStructurePieceTypes.STRUCTURE_PIECE_TYPES,
			ChunkBlanketProcessors.CHUNK_BLANKETING_TYPES,
			TFStructurePlacementTypes.STRUCTURE_PLACEMENT_TYPES,
			TemplateMarkerHandlers.TEMPLATE_MARKER_HANDLER_TYPES
		));

		for (ResourceKey<? extends Registry<?>> key : BuiltInRegistries.REGISTRY.registryKeySet()) {
			MOD_BUS.post(new RegisterEvent(key));
		}
		TFRemapper.addRegistryAliases();

		MOD_BUS.post(new EntityAttributeCreationEvent());
		MOD_BUS.post(new RegisterSpawnPlacementsEvent());
		MOD_BUS.post(new RegisterPayloadHandlersEvent());
		TFDataMapLoader.init(MOD_BUS.post(new RegisterDataMapTypesEvent()));
		MOD_BUS.post(new BlockEntityTypeAddBlocksEvent());
		MOD_BUS.post(new RegisterCapabilitiesEvent());
		CreativeModeTabEvents.MODIFY_OUTPUT_ALL.register((tab, output) ->
			BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(tab).ifPresent(key -> MOD_BUS.post(new BuildCreativeModeTabContentsEvent(key, output))));

		MOD_BUS.post(new FMLCommonSetupEvent());
		MOD_BUS.post(new InterModEnqueueEvent());

		net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.clientboundPlay().register(twilightforest.fabric.SpawnDataPayload.TYPE, twilightforest.fabric.SpawnDataPayload.STREAM_CODEC);
		NeoForgeEventBridge.init();
		twilightforest.fabric.GlobalLootModifierLoader.init();
		twilightforest.fabric.DevSmokeTest.init();

		if (ModList.get().isLoaded("trinkets")) {
			loadTrinketsCompat(MOD_BUS);
		}
	}

	/**
	 * Registries whose entries other registries' suppliers read, in the order they must be filled. Everything
	 * else registers afterwards in the order given. Mirrors the order NeoForge fires RegisterEvent in closely
	 * enough for Twilight Forest's cross-references (block entities need blocks, spawn eggs need entity types...).
	 */
	private static final List<ResourceKey<? extends Registry<?>>> REGISTRY_PRIORITY = List.of(
		net.minecraft.core.registries.Registries.SOUND_EVENT,
		net.minecraft.core.registries.Registries.DATA_COMPONENT_TYPE,
		net.minecraft.core.registries.Registries.ATTRIBUTE,
		net.minecraft.core.registries.Registries.MOB_EFFECT,
		net.minecraft.core.registries.Registries.PARTICLE_TYPE,
		net.minecraft.core.registries.Registries.BLOCK,
		net.minecraft.core.registries.Registries.FLUID,
		net.minecraft.core.registries.Registries.ENTITY_TYPE,
		net.minecraft.core.registries.Registries.ITEM
	);

	private static void registerInPriorityOrder(List<DeferredRegister<?>> registers) {
		List<DeferredRegister<?>> sorted = new java.util.ArrayList<>(registers);
		sorted.sort(java.util.Comparator.comparingInt(reg -> {
			int i = REGISTRY_PRIORITY.indexOf(reg.getRegistryKey());
			return i < 0 ? REGISTRY_PRIORITY.size() : i;
		}));
		for (DeferredRegister<?> register : sorted) {
			register.register(MOD_BUS);
		}
	}

	private void loadConfigs() {
		try {
			Reflection.initialize(ConfigSetup.class);
		} catch (Throwable t) {
			LOGGER.error("Failed to set up configs", t);
			return;
		}
		Path dir = FabricLoader.getInstance().getConfigDir();
		for (ModConfig config : ModLoadingContext.get().getActiveContainer().getConfigs()) {
			if (config.getType() == ModConfig.Type.CLIENT && FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT)
				continue;
			config.getSpec().loadAndSave(dir.resolve(ID + "-" + config.getType().name().toLowerCase(Locale.ROOT) + ".json"));
			MOD_BUS.post(new ModConfigEvent.Loading(config));
		}
	}

	/**
	 * Curios compat, ported to Trinkets. Runs at the end of common init; the client listeners fire when the client
	 * entrypoint posts its registration events afterwards.
	 */
	private static void loadTrinketsCompat(IEventBus bus) {
		try {
			twilightforest.compat.trinkets.TrinketsCompat.init();
			if (FMLEnvironment.getDist().isClient()) {
				bus.addListener(twilightforest.port.neoforged.fml.event.lifecycle.FMLClientSetupEvent.class, twilightforest.compat.trinkets.TrinketsClientCompat::registerTrinketRenderers);
				bus.addListener(twilightforest.port.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions.class, twilightforest.compat.trinkets.TrinketsClientCompat::registerTrinketLayers);
				bus.addListener(twilightforest.port.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent.class, twilightforest.compat.trinkets.TrinketsClientCompat::registerCustomRenderData);
			}
		} catch (Throwable t) {
			LOGGER.warn("Failed to load Trinkets compat: {}", t.getMessage());
		}
	}

	public static Identifier prefix(String name) {
		return Identifier.fromNamespaceAndPath(ID, name.toLowerCase(Locale.ROOT));
	}

	public static Identifier getModelTexture(String name) {
		return Identifier.fromNamespaceAndPath(ID, MODEL_DIR + name);
	}

	public static Identifier getGuiTexture(String name) {
		return Identifier.fromNamespaceAndPath(ID, GUI_DIR + name);
	}

	public static Identifier getEnvTexture(String name) {
		return Identifier.fromNamespaceAndPath(ID, ENVIRO_DIR + name);
	}
}
