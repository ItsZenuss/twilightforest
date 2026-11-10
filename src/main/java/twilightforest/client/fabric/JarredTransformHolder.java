package twilightforest.client.fabric;

import net.minecraft.client.resources.model.cuboid.ItemTransform;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;

/**
 * Implemented on ItemTransforms by a mixin: stores the model's "twilightforest:jarred" display transform.
 */
public interface JarredTransformHolder {
	String KEY = "twilightforest:jarred";

	@Nullable
	ItemTransform twilightforest$getJarred();

	void twilightforest$setJarred(@Nullable ItemTransform transform);

	static ItemDisplayContext jarred() {
		return ItemDisplayContext.valueOf("TWILIGHTFOREST_JARRED");
	}
}
