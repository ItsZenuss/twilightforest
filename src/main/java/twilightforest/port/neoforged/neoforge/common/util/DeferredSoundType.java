package twilightforest.port.neoforged.neoforge.common.util;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;

import java.util.function.Supplier;

public class DeferredSoundType extends SoundType {
	private final Supplier<SoundEvent> breakSoundSupplier;
	private final Supplier<SoundEvent> stepSoundSupplier;
	private final Supplier<SoundEvent> placeSoundSupplier;
	private final Supplier<SoundEvent> hitSoundSupplier;
	private final Supplier<SoundEvent> fallSoundSupplier;

	public DeferredSoundType(
		float volume,
		float pitch,
		Supplier<SoundEvent> breakSound,
		Supplier<SoundEvent> stepSound,
		Supplier<SoundEvent> placeSound,
		Supplier<SoundEvent> hitSound,
		Supplier<SoundEvent> fallSound
	) {
		super(volume, pitch, null, null, null, null, null);
		this.breakSoundSupplier = breakSound;
		this.stepSoundSupplier = stepSound;
		this.placeSoundSupplier = placeSound;
		this.hitSoundSupplier = hitSound;
		this.fallSoundSupplier = fallSound;
	}

	@Override
	public SoundEvent getBreakSound() {
		return breakSoundSupplier.get();
	}

	@Override
	public SoundEvent getStepSound() {
		return stepSoundSupplier.get();
	}

	@Override
	public SoundEvent getPlaceSound() {
		return placeSoundSupplier.get();
	}

	@Override
	public SoundEvent getHitSound() {
		return hitSoundSupplier.get();
	}

	@Override
	public SoundEvent getFallSound() {
		return fallSoundSupplier.get();
	}
}
