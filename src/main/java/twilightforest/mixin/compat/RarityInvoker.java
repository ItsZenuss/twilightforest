package twilightforest.mixin.compat;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(net.minecraft.world.item.Rarity.class)
public interface RarityInvoker {
	@Invoker("<init>")
	static net.minecraft.world.item.Rarity twilightforest$create(String internalName, int ordinal, int id, String name, net.minecraft.ChatFormatting color) {
		throw new AssertionError();
	}
}
