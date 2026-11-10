package twilightforest.fabric;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.registry.CompostableRegistry;
import net.fabricmc.fabric.api.registry.FuelValueEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import twilightforest.port.neoforged.neoforge.registries.datamaps.DataMapType;
import twilightforest.port.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import twilightforest.TwilightForestMod;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Fabric port of NeoForge's data maps. Loads {@code data/<ns>/data_maps/<registry>/<name>.json} for every
 * registered {@link DataMapType} on each datapack reload, and applies Twilight Forest's entries for NeoForge's
 * built-in compostable and furnace-fuel maps to Fabric's registries.
 * <p>
 * Limitation: values are not synced to clients, so on a dedicated server client-only uses (magic map colours)
 * fall back to defaults.
 */
public final class TFDataMapLoader implements ResourceManagerReloadListener {
	private final List<DataMapType<?, ?>> types;
	private static List<DataMapType<?, ?>> registeredTypes = List.of();

	private TFDataMapLoader(List<DataMapType<?, ?>> types) {
		this.types = types;
	}

	public static void init(RegisterDataMapTypesEvent event) {
		List<DataMapType<?, ?>> types = List.copyOf(event.getRegistered());
		registeredTypes = types;
		net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry.clientboundPlay().register(SyncPayload.TYPE, SyncPayload.STREAM_CODEC);
		net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) ->
			net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.send(player, encode(player.registryAccess())));
		ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(TwilightForestMod.prefix("data_maps"), new TFDataMapLoader(types));
		registerCompostables();
		registerFuels();
		registerParrotImitations();
	}

	private static String folderFor(ResourceKey<?> registryKey) {
		Identifier id = registryKey.identifier();
		return id.getNamespace().equals(Identifier.DEFAULT_NAMESPACE) ? id.getPath() : id.getNamespace() + "/" + id.getPath();
	}

	@Override
	public void onResourceManagerReload(ResourceManager manager) {
		for (DataMapType<?, ?> type : this.types) {
			this.load(manager, type);
		}
	}

	private <R, T> void load(ResourceManager manager, DataMapType<R, T> type) {
		type.clear();
		Identifier file = Identifier.fromNamespaceAndPath(type.getId().getNamespace(), "data_maps/" + folderFor(type.getRegistryKey()) + "/" + type.getId().getPath() + ".json");
		for (Resource resource : manager.getResourceStack(file)) {
			try (Reader reader = resource.openAsReader()) {
				JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
				if (root.has("replace") && root.get("replace").getAsBoolean()) type.clear();
				readValues(root, (key, element) -> type.getCodec().parse(JsonOps.INSTANCE, element)
					.resultOrPartial(err -> TwilightForestMod.LOGGER.warn("Bad data map entry {} in {}: {}", key, file, err))
					.ifPresent(value -> put(type, key, value)));
			} catch (Exception e) {
				TwilightForestMod.LOGGER.error("Failed to load data map {}", file, e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static <R, T> void put(DataMapType<R, T> type, String key, T value) {
		ResourceKey<Registry<R>> registry = (ResourceKey<Registry<R>>) type.getRegistryKey();
		if (key.startsWith("#")) {
			type.putTag(TagKey.create(registry, Identifier.parse(key.substring(1))), value);
		} else {
			type.put(ResourceKey.create(registry, Identifier.parse(key)), value);
		}
	}

	private interface EntryConsumer {
		void accept(String key, JsonElement value);
	}

	private static void readValues(JsonObject root, EntryConsumer consumer) {
		if (!root.has("values")) return;
		for (Map.Entry<String, JsonElement> entry : root.getAsJsonObject("values").entrySet()) {
			JsonElement value = entry.getValue();
			// NeoForge allows {"value": ..., "replace": bool} wrappers around an entry
			if (value.isJsonObject()) {
				JsonObject obj = value.getAsJsonObject();
				if (obj.has("value") && (obj.size() == 1 || (obj.size() == 2 && obj.has("replace")))) {
					value = obj.get("value");
				}
			}
			consumer.accept(entry.getKey(), value);
		}
	}

	private static JsonObject readBundled(String path) {
		try (InputStream in = TFDataMapLoader.class.getResourceAsStream("/data/neoforge/data_maps/" + path)) {
			if (in == null) return null;
			return JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
		} catch (Exception e) {
			TwilightForestMod.LOGGER.error("Failed reading bundled data map {}", path, e);
			return null;
		}
	}

	private static void registerCompostables() {
		JsonObject root = readBundled("item/compostables.json");
		if (root == null) return;
		readValues(root, (key, value) -> {
			if (key.startsWith("#")) return;
			BuiltInRegistries.ITEM.getOptional(Identifier.parse(key)).ifPresent(item ->
				CompostableRegistry.INSTANCE.add(item, value.getAsJsonObject().get("chance").getAsFloat()));
		});
	}

	/**
	 * NeoForge's parrot_imitations data map: sounds parrots make to imitate Twilight Forest mobs.
	 */
	private static void registerParrotImitations() {
		JsonObject root = readBundled("entity_type/parrot_imitations.json");
		if (root == null) return;
		readValues(root, (key, value) -> {
			if (key.startsWith("#")) return;
			BuiltInRegistries.ENTITY_TYPE.getOptional(Identifier.parse(key)).ifPresent(type -> {
				Identifier sound = Identifier.parse(value.getAsJsonObject().get("sound").getAsString());
				BuiltInRegistries.SOUND_EVENT.getOptional(sound).ifPresent(event ->
					net.minecraft.world.entity.animal.parrot.Parrot.MOB_SOUND_MAP.put(type, event));
			});
		});
	}

	private static void registerFuels() {
		JsonObject root = readBundled("item/furnace_fuels.json");
		if (root == null) return;
		FuelValueEvents.BUILD.register((builder, context) -> readValues(root, (key, value) -> {
			int burnTime = value.getAsJsonObject().get("burn_time").getAsInt();
			if (key.startsWith("#")) {
				builder.add(TagKey.<Item>create(Registries.ITEM, Identifier.parse(key.substring(1))), burnTime);
			} else {
				BuiltInRegistries.ITEM.getOptional(Identifier.parse(key)).ifPresent(item -> builder.add(item, burnTime));
			}
		}));
	}

	/**
	 * Data maps are only loaded on the server; this payload carries them to remote clients (NeoForge synced them too).
	 */
	public record SyncPayload(String json) implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {
		public static final Type<SyncPayload> TYPE = new Type<>(TwilightForestMod.prefix("data_map_sync"));
		public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.FriendlyByteBuf, SyncPayload> STREAM_CODEC =
			net.minecraft.network.codec.ByteBufCodecs.stringUtf8(Integer.MAX_VALUE).map(SyncPayload::new, SyncPayload::json).cast();

		@Override
		public Type<? extends net.minecraft.network.protocol.common.custom.CustomPacketPayload> type() {
			return TYPE;
		}
	}

	private static SyncPayload encode(net.minecraft.core.HolderLookup.Provider registries) {
		com.mojang.serialization.DynamicOps<JsonElement> ops = registries.createSerializationContext(JsonOps.INSTANCE);
		JsonObject root = new JsonObject();
		for (DataMapType<?, ?> type : registeredTypes) {
			root.add(type.getId().toString(), encodeType(type, ops));
		}
		return new SyncPayload(root.toString());
	}

	private static <R, T> JsonObject encodeType(DataMapType<R, T> type, com.mojang.serialization.DynamicOps<JsonElement> ops) {
		JsonObject values = new JsonObject();
		type.getData().forEach((key, value) -> type.getCodec().encodeStart(ops, value).result()
			.ifPresent(json -> values.add(key.identifier().toString(), json)));
		type.getTagData().forEach((tag, value) -> type.getCodec().encodeStart(ops, value).result()
			.ifPresent(json -> values.add("#" + tag.location(), json)));
		JsonObject wrapper = new JsonObject();
		wrapper.add("values", values);
		return wrapper;
	}

	/**
	 * Client side: applies synced data maps unless this client also runs the server (they share the same maps).
	 */
	public static void applySync(SyncPayload payload, net.minecraft.core.HolderLookup.Provider registries, boolean integratedServer) {
		if (integratedServer) return;
		com.mojang.serialization.DynamicOps<JsonElement> ops = registries.createSerializationContext(JsonOps.INSTANCE);
		JsonObject root = JsonParser.parseString(payload.json()).getAsJsonObject();
		for (DataMapType<?, ?> type : registeredTypes) {
			type.clear();
			if (root.has(type.getId().toString())) decodeType(type, root.getAsJsonObject(type.getId().toString()), ops);
		}
	}

	private static <R, T> void decodeType(DataMapType<R, T> type, JsonObject json, com.mojang.serialization.DynamicOps<JsonElement> ops) {
		readValues(json, (key, element) -> type.getCodec().parse(ops, element).result().ifPresent(value -> put(type, key, value)));
	}
}
