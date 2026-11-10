package twilightforest.mixin.client;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.resources.model.cuboid.ItemTransform;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.client.fabric.JarredTransformHolder;

import java.lang.reflect.Type;

/**
 * Fabric port: reads the "twilightforest:jarred" entry of a model's display block.
 */
@Mixin(targets = "net.minecraft.client.resources.model.cuboid.ItemTransforms$Deserializer")
public abstract class ItemTransformsDeserializerMixin {
	@ModifyReturnValue(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/resources/model/cuboid/ItemTransforms;", at = @At("RETURN"))
	private ItemTransforms twilightforest$readJarred(ItemTransforms transforms, JsonElement json, Type type, JsonDeserializationContext context) {
		if (json.isJsonObject() && json.getAsJsonObject().has(JarredTransformHolder.KEY)) {
			ItemTransform jarred = context.deserialize(json.getAsJsonObject().get(JarredTransformHolder.KEY), ItemTransform.class);
			ItemTransforms copy = new ItemTransforms(transforms.thirdPersonLeftHand(), transforms.thirdPersonRightHand(), transforms.firstPersonLeftHand(),
				transforms.firstPersonRightHand(), transforms.head(), transforms.gui(), transforms.ground(), transforms.fixed(), transforms.fixedFromBottom());
			((JarredTransformHolder) (Object) copy).twilightforest$setJarred(jarred);
			return copy;
		}
		return transforms;
	}
}
