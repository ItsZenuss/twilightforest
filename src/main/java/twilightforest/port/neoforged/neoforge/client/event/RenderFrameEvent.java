package twilightforest.port.neoforged.neoforge.client.event;

import twilightforest.port.neoforged.bus.api.Event;

public class RenderFrameEvent extends Event {
	public static class Pre extends RenderFrameEvent {}
	public static class Post extends RenderFrameEvent {}
}
