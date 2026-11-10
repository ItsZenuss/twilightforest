package twilightforest.port.neoforged.neoforge.client.extensions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelResolver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface IMinecraftExtension {
	Map<Object, BlockModelResolver> RESOLVERS = new ConcurrentHashMap<>();

	default BlockModelResolver getBlockModelResolver() {
		if (this instanceof Minecraft mc) {
			return RESOLVERS.computeIfAbsent(mc, m -> new BlockModelResolver(mc.getModelManager()));
		}
		return null;
	}
}
