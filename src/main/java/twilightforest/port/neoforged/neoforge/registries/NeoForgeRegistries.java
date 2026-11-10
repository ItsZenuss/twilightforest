package twilightforest.port.neoforged.neoforge.registries;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import twilightforest.port.neoforged.neoforge.attachment.AttachmentType;
import twilightforest.port.neoforged.neoforge.common.loot.IGlobalLootModifier;

public class NeoForgeRegistries {
	public static final ResourceKey<Registry<net.minecraft.network.syncher.EntityDataSerializer<?>>> ENTITY_DATA_SERIALIZERS = Keys.ENTITY_DATA_SERIALIZERS;

	public static final class Keys {
		public static final ResourceKey<Registry<net.minecraft.network.syncher.EntityDataSerializer<?>>> ENTITY_DATA_SERIALIZERS = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("neoforge", "entity_data_serializers"));
		public static final ResourceKey<Registry<AttachmentType<?>>> ATTACHMENT_TYPES = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("neoforge", "attachment_types"));
		public static final ResourceKey<Registry<MapCodec<? extends IGlobalLootModifier>>> GLOBAL_LOOT_MODIFIER_SERIALIZERS = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("neoforge", "global_loot_modifier_serializers"));
		public static final ResourceKey<Registry<MapCodec<? extends twilightforest.port.neoforged.neoforge.common.conditions.ICondition>>> CONDITION_CODECS = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("neoforge", "condition_codecs"));
	}
}
