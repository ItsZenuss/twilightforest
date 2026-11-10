package twilightforest.mixin.compat;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(net.minecraft.world.item.ItemDisplayContext.class)
public interface ItemDisplayContextInvoker {
	@Invoker("<init>")
	static net.minecraft.world.item.ItemDisplayContext twilightforest$create(String internalName, int ordinal, int id, String name) {
		throw new AssertionError();
	}
}
