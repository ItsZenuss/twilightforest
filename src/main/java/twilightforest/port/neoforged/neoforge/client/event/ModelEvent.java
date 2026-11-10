package twilightforest.port.neoforged.neoforge.client.event;

import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.UnbakedExtraModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.port.neoforged.bus.api.Event;
import twilightforest.port.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import twilightforest.port.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

import java.util.HashMap;
import java.util.Map;

/**
 * Fabric port of NeoForge's ModelEvent. RegisterStandalone is fired from a Fabric ModelLoadingPlugin on every
 * model reload. ModifyBakingResult has no Fabric equivalent and is not fired.
 */
public class ModelEvent extends Event {
	public static class ModifyBakingResult extends ModelEvent {
		private final BakingResult bakingResult = new BakingResult();

		public BakingResult getBakingResult() {
			return bakingResult;
		}

		public static class BakingResult {
			private final Map<BlockState, BlockStateModel> blockStateModels = new HashMap<>();

			public Map<BlockState, BlockStateModel> blockStateModels() {
				return blockStateModels;
			}
		}
	}

	public static class RegisterStandalone extends ModelEvent {
		private final ModelLoadingPlugin.Context context;

		public RegisterStandalone(ModelLoadingPlugin.Context context) {
			this.context = context;
		}

		@SuppressWarnings({"unchecked", "rawtypes"})
		public void register(StandaloneModelKey<?> key, SimpleUnbakedStandaloneModel model) {
			this.context.addModel((ExtraModelKey) key.fabricKey(), (UnbakedExtraModel) model.toFabric());
		}
	}
}
