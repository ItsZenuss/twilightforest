package twilightforest.fabric;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import twilightforest.port.neoforged.neoforge.common.loot.IGlobalLootModifier;
import twilightforest.port.neoforged.neoforge.common.loot.LootModifier;
import twilightforest.port.neoforged.neoforge.registries.NeoForgeRegistries;
import twilightforest.TwilightForestMod;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Fabric port of NeoForge's global loot modifiers: loads {@code data/<ns>/loot_modifiers/*.json} (dispatched by
 * {@code "type"} through the global loot modifier serializer registry) and applies them to every generated loot
 * drop through Fabric's LootTableEvents.MODIFY_DROPS.
 */
public final class GlobalLootModifierLoader extends SimpleJsonResourceReloadListener<JsonElement> {
	private static volatile List<IGlobalLootModifier> modifiers = List.of();

	private GlobalLootModifierLoader() {
		super(ExtraCodecs.JSON, FileToIdConverter.json("loot_modifiers"));
	}

	public static void init() {
		ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(TwilightForestMod.prefix("global_loot_modifiers"), new GlobalLootModifierLoader());
		LootTableEvents.MODIFY_DROPS.register((table, context, drops) -> {
			List<IGlobalLootModifier> active = modifiers;
			if (active.isEmpty()) return;
			ObjectArrayList<ItemStack> loot = new ObjectArrayList<>(drops);
			for (IGlobalLootModifier modifier : active) {
				loot = modifier.apply(loot, context);
			}
			drops.clear();
			drops.addAll(loot);
		});
	}

	@SuppressWarnings("unchecked")
	private static Codec<IGlobalLootModifier> dispatchCodec() {
		Registry<MapCodec<? extends IGlobalLootModifier>> registry = (Registry<MapCodec<? extends IGlobalLootModifier>>) BuiltInRegistries.REGISTRY.getValue(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS.identifier());
		if (registry == null) throw new IllegalStateException("Global loot modifier serializer registry missing");
		return registry.byNameCodec().dispatch(IGlobalLootModifier::codec, codec -> (MapCodec<IGlobalLootModifier>) codec);
	}

	@Override
	protected void apply(Map<Identifier, JsonElement> files, ResourceManager manager, ProfilerFiller profiler) {
		Codec<IGlobalLootModifier> codec = dispatchCodec();
		List<IGlobalLootModifier> loaded = new ArrayList<>();
		files.forEach((id, json) -> codec.parse(((RegistryAwareJsonOps) (Object) this).twilightforest$getOps(), json)
			.resultOrPartial(error -> TwilightForestMod.LOGGER.error("Failed to load loot modifier {}: {}", id, error))
			.ifPresent(loaded::add));
		loaded.sort(Comparator.comparingInt(m -> m instanceof LootModifier lm ? lm.priority() : 0));
		modifiers = List.copyOf(loaded);
		TwilightForestMod.LOGGER.info("Loaded {} global loot modifiers", loaded.size());
	}
}
