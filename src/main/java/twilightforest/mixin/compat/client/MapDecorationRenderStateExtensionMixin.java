package twilightforest.mixin.compat.client;

import net.minecraft.core.Holder;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import twilightforest.port.neoforged.neoforge.client.extensions.IMapRenderStateExtension;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Fabric port: per-instance storage for NeoForge's render-state data (the shim interface's default would share
 * one map between every render state).
 */
@Mixin(net.minecraft.client.renderer.state.MapRenderState.MapDecorationRenderState.class)
public abstract class MapDecorationRenderStateExtensionMixin implements IMapRenderStateExtension {
	@Unique
	@Nullable
	private Map<ContextKey<?>, Object> tf$renderData;

	@Unique
	@Nullable
	private Holder<MapDecorationType> tf$type;

	@Override
	public Holder<MapDecorationType> getType() {
		return this.tf$type;
	}

	@Override
	public void setType(Holder<MapDecorationType> type) {
		this.tf$type = type;
	}

	@Override
	public <T> void setRenderData(ContextKey<T> key, @Nullable T value) {
		if (value == null) {
			if (this.tf$renderData != null) this.tf$renderData.remove(key);
			return;
		}
		if (this.tf$renderData == null) this.tf$renderData = new IdentityHashMap<>();
		this.tf$renderData.put(key, value);
	}

	@Override
	@SuppressWarnings("unchecked")
	@Nullable
	public <T> T getRenderData(ContextKey<T> key) {
		return this.tf$renderData == null ? null : (T) this.tf$renderData.get(key);
	}
}
