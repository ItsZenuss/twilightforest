package twilightforest.port.neoforged.neoforge.common.loot;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public interface IGlobalLootModifier {
	MapCodec<? extends IGlobalLootModifier> codec();
	ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootContext context);
}
