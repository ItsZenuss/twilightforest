package twilightforest.port.neoforged.neoforge.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class Tags {
	private Tags() {}

	public static final class Items {
		public static final TagKey<Item> DUSTS_REDSTONE = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "dusts/redstone"));
		public static final TagKey<Item> ORES = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "ores"));
		public static final TagKey<Item> GEMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "gems"));
		public static final TagKey<Item> INGOTS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "ingots"));
		public static final TagKey<Item> RAW_MATERIALS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "raw_materials"));
		public static final TagKey<Item> RODS_WOODEN = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "rods/wooden"));
		public static final TagKey<Item> CROPS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "crops"));
		public static final TagKey<Item> TOOLS_SHIELD = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "tools/shield"));
		public static final TagKey<Item> TOOLS_BOW = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "tools/bow"));
		public static final TagKey<Item> TOOLS_CROSSBOW = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "tools/crossbow"));
		public static final TagKey<Item> TOOLS_FISHING_ROD = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "tools/fishing_rod"));
		public static final TagKey<Item> ARMORS_HUMANOID = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "armors"));
	}

	public static final class Blocks {
		public static final TagKey<Block> ORES = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores"));
		public static final TagKey<Block> STORAGE_BLOCKS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "storage_blocks"));
	}

	public static final class EntityTypes {
		public static final TagKey<net.minecraft.world.entity.EntityType<?>> BOSSES = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("c", "bosses"));
	}
}
