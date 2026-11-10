package twilightforest.port.neoforged.neoforge.client.event;

import twilightforest.port.neoforged.bus.api.Event;

public class ClientTickEvent extends Event {
	public static class Pre extends ClientTickEvent {}
	public static class Post extends ClientTickEvent {}
}
