package twilightforest.port.neoforged.neoforge.client.event;

import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import twilightforest.port.neoforged.bus.api.Event;

import java.util.function.Function;

public class RegisterParticleProvidersEvent extends Event {
	public <T extends ParticleOptions> void registerSpecial(ParticleType<T> type, ParticleProvider<T> provider) {
		ParticleProviderRegistry.getInstance().register(type, provider);
	}

	public <T extends ParticleOptions> void registerSpriteSet(ParticleType<T> type, Function<SpriteSet, ? extends ParticleProvider<T>> reg) {
		ParticleProviderRegistry.getInstance().register(type, (ParticleProviderRegistry.PendingParticleProvider<T>) sprites -> reg.apply(sprites));
	}
}
