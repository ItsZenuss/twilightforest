package twilightforest.port.neoforged.neoforge.common.conditions;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;

/**
 * Fabric port: exposes a NeoForge {@link ICondition} codec as a Fabric resource condition with the same id, so
 * {@code fabric:load_conditions} entries like {@code {"condition": "twilightforest:uncrafting_table_enabled"}} work.
 */
public final class FabricConditionBridge {
	private FabricConditionBridge() {}

	@SuppressWarnings("unchecked")
	public static void register(Identifier id, MapCodec<? extends ICondition> codec) {
		Holder[] typeRef = new Holder[1];
		MapCodec<Wrapped> wrappedCodec = ((MapCodec<ICondition>) codec).xmap(c -> new Wrapped(c, typeRef[0].type), Wrapped::condition);
		ResourceConditionType<Wrapped> type = ResourceConditionType.create(id, wrappedCodec);
		typeRef[0] = new Holder(type);
		ResourceConditions.register(type);
	}

	private record Holder(ResourceConditionType<Wrapped> type) {}

	private record Wrapped(ICondition condition, ResourceConditionType<Wrapped> type) implements ResourceCondition {
		@Override
		public ResourceConditionType<?> getType() {
			return this.type;
		}

		@Override
		public boolean test(RegistryOps.RegistryInfoLookup registryInfo) {
			return this.condition.test(new ICondition.IContext() {});
		}
	}
}
