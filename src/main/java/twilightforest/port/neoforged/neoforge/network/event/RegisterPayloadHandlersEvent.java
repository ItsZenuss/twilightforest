package twilightforest.port.neoforged.neoforge.network.event;

import twilightforest.port.neoforged.neoforge.network.registration.PayloadRegistrar;

public class RegisterPayloadHandlersEvent {
	public PayloadRegistrar registrar(String namespace) {
		return new PayloadRegistrar(namespace);
	}
}
