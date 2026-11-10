package twilightforest.compat.trinkets;

import eu.pb4.trinkets.api.TrinketAttachment;
import eu.pb4.trinkets.api.TrinketDropRule;
import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.TrinketsApi;
import eu.pb4.trinkets.api.callback.TrinketCallback;
import eu.pb4.trinkets.api.event.TrinketDropCallback;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import twilightforest.port.neoforged.neoforge.network.PacketDistributor;
import twilightforest.events.CharmEvents;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFItems;
import twilightforest.network.CreateMovingCicadaSoundPacket;
import twilightforest.util.TFItemStackUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Fabric port of Twilight Forest's Curios compat, targeting Trinkets (Curios has no Fabric build). Charms of Life go
 * in the necklace slot, Charms of Keeping in the belt slot, and trophies, critters and skull candles in the hat slot
 * (see {@code data/trinkets/tags/item}).
 */
public class TrinketsCompat {

	static List<ItemLike> wearables() {
		return List.of(
			TFItems.CHARM_OF_KEEPING_1.get(), TFItems.CHARM_OF_KEEPING_2.get(), TFItems.CHARM_OF_KEEPING_3.get(), TFItems.CHARM_OF_LIFE_1.get(), TFItems.CHARM_OF_LIFE_2.get(),
			TFItems.NAGA_TROPHY.get(), TFItems.LICH_TROPHY.get(), TFItems.MINOSHROOM_TROPHY.get(), TFItems.HYDRA_TROPHY.get(), TFItems.KNIGHT_PHANTOM_TROPHY.get(),
			TFItems.UR_GHAST_TROPHY.get(), TFItems.ALPHA_YETI_TROPHY.get(), TFItems.SNOW_QUEEN_TROPHY.get(), TFItems.QUEST_RAM_TROPHY.get(),
			TFBlocks.CICADA.get(), TFBlocks.FIREFLY.get(), TFBlocks.MOONWORM.get(), TFItems.SKELETON_SKULL_CANDLE.get(), TFItems.WITHER_SKELETON_SKULL_CANDLE.get(),
			TFItems.ZOMBIE_SKULL_CANDLE.get(), TFItems.CREEPER_SKULL_CANDLE.get(), TFItems.PLAYER_SKULL_CANDLE.get(), TFItems.PIGLIN_SKULL_CANDLE.get()
		);
	}

	/**
	 * Common setup: equip behaviour for every wearable item, and keeping trinkets on death with a Charm of Keeping.
	 */
	public static void init() {
		TrinketCallback callback = new TrinketCallback() {
			@Override
			public Holder<SoundEvent> getEquipSound(ItemStack stack, TrinketSlotAccess slot, LivingEntity entity) {
				return SoundEvents.ARMOR_EQUIP_GENERIC;
			}

			@Override
			public void onEquip(ItemStack stack, TrinketSlotAccess slot, LivingEntity entity) {
				//check that we don't have a cicada already on our head before trying to start the sound
				if (!entity.getItemBySlot(EquipmentSlot.HEAD).is(TFBlocks.CICADA.get().asItem())) {
					if (stack.is(TFBlocks.CICADA.get().asItem()) && !entity.level().isClientSide()) {
						PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new CreateMovingCicadaSoundPacket(entity.getId()));
					}
				}
			}

			@Override
			public boolean canEquipFromUse(ItemStack stack, LivingEntity entity) {
				return true;
			}
		};
		for (ItemLike item : wearables()) {
			TrinketCallback.setCallback(item.asItem(), callback);
		}
		TrinketDropCallback.EVENT.register(TrinketsCompat::keepTrinkets);
	}

	//if we have any trinkets and die with a charm of keeping on us, keep our trinkets instead of dropping them
	private static TrinketDropRule keepTrinkets(TrinketDropRule rule, ItemStack stack, TrinketSlotAccess slot, LivingEntity entity) {
		if (entity instanceof Player player && !player.level().isClientSide()) {
			CompoundTag playerData = CharmEvents.getPlayerData(player);
			if (playerData.contains(CharmEvents.CONSUMED_CHARM_TAG) && player.hasData(TFDataAttachments.CHARM_INVENTORY) && !player.getData(TFDataAttachments.CHARM_INVENTORY).isEmpty()) {
				return TrinketDropRule.KEEP;
			}
		}
		return rule;
	}

	private static Optional<TrinketSlotAccess> findFirst(LivingEntity entity, Predicate<ItemStack> stackPredicate) {
		TrinketAttachment attachment = TrinketsApi.getAttachment(entity);
		return attachment == null ? Optional.empty() : attachment.findFirst(stackPredicate);
	}

	public static boolean isTrinketEquipped(LivingEntity entity, Predicate<ItemStack> stackPredicate) {
		return findFirst(entity, stackPredicate).isPresent();
	}

	public static boolean isTrinketEquippedAndVisible(LivingEntity entity, Predicate<ItemStack> stackPredicate) {
		return findFirst(entity, stackPredicate).filter(TrinketSlotAccess::isVisible).isPresent();
	}

	/**
	 * Development smoke test only: puts one item of each kind into its trinket slot, checking the slot accepts it.
	 */
	public static void equipForSmokeTest(Player player) {
		TrinketAttachment attachment = TrinketsApi.getAttachment(player);
		java.util.Map<String, Item> wanted = java.util.Map.of("head/hat", TFItems.NAGA_TROPHY.get(), "chest/necklace", TFItems.CHARM_OF_LIFE_2.get(), "legs/belt", TFItems.CHARM_OF_KEEPING_1.get());
		java.util.List<String> results = new java.util.ArrayList<>();
		attachment.getInventories().forEach((key, inventory) -> {
			for (java.util.Map.Entry<String, Item> entry : wanted.entrySet()) {
				if (!key.equals(entry.getKey()) && !key.equals(entry.getKey().replace('/', ':'))) continue;
				ItemStack stack = new ItemStack(entry.getValue());
				TrinketSlotAccess access = inventory.getOrCreateSlotAccess(0);
				boolean valid = inventory.slotType().validatorCheck(stack, access, player);
				boolean set = valid && access.set(stack);
				results.add(key + "=" + (set ? "equipped" : valid ? "set failed" : "rejected"));
			}
		});
		twilightforest.TwilightForestMod.LOGGER.info("[smoketest] Trinkets: {} (slots: {})", results, attachment.getInventories().keySet());
	}

	public static boolean findAndConsumeTrinket(Item item, Player player, boolean saveItemToTag) {
		Optional<TrinketSlotAccess> slot = findFirst(player, stack -> stack.is(item));
		return slot.isPresent() && TFItemStackUtils.consumeInventoryItem(
			slot.get().get(),
			item,
			CharmEvents.getPlayerData(player),
			saveItemToTag,
			player.registryAccess()
		);
	}
}
