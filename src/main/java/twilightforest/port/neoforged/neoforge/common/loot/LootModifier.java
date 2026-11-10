package twilightforest.port.neoforged.neoforge.common.loot;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public abstract class LootModifier implements IGlobalLootModifier {
	protected final LootItemCondition[] conditions;
	protected final int priority;

	public LootModifier(LootItemCondition[] conditions, int priority) {
		this.conditions = conditions;
		this.priority = priority;
	}

	public static <T extends LootModifier> RecordCodecBuilder<T, LootItemCondition[]> conditionsField() {
		return LootItemCondition.DIRECT_CODEC.listOf().fieldOf("conditions").xmap(
			l -> l.toArray(new LootItemCondition[0]),
			List::of
		).forGetter(m -> m.conditions);
	}

	public static <T extends LootModifier> RecordCodecBuilder<T, Integer> priorityField() {
		return Codec.INT.optionalFieldOf("priority", 0).forGetter(m -> m.priority);
	}

	public static <T extends LootModifier> Products.P2<RecordCodecBuilder.Mu<T>, LootItemCondition[], Integer> codecStart(RecordCodecBuilder.Instance<T> instance) {
		return instance.group(conditionsField(), priorityField());
	}

	@Override
	public ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		for (LootItemCondition condition : this.conditions) {
			if (!condition.test(context)) return generatedLoot;
		}
		return doApply(generatedLoot, context);
	}

	public int priority() {
		return this.priority;
	}

	protected abstract ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context);
}
