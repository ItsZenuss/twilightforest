package twilightforest.port.neoforged.neoforge.client.event;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
import twilightforest.port.neoforged.bus.api.Event;

/**
 * Fabric port: registers pipelines with vanilla so they're precompiled with the built-in ones.
 */
public class RegisterRenderPipelinesEvent extends Event {
	public void registerPipeline(Object pipeline) {
		RenderPipelines.register((RenderPipeline) pipeline);
	}
}
