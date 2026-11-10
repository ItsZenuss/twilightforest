package twilightforest.port.neoforged.neoforge.common.conditions;

import com.mojang.serialization.MapCodec;

public interface ICondition {
	MapCodec<? extends ICondition> codec();
	boolean test(IContext context);

	interface IContext {
	}
}
