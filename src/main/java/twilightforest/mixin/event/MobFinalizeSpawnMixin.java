package twilightforest.mixin.event;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Fabric port: fires NeoForge's FinalizeSpawnEvent when a mob finishes its spawn setup.
 */
@Mixin(Mob.class)
public abstract class MobFinalizeSpawnMixin {
	@Inject(method = "finalizeSpawn", at = @At("TAIL"))
	private void tf$fireFinalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason reason, SpawnGroupData data, CallbackInfoReturnable<SpawnGroupData> cir) {
		NeoForge.EVENT_BUS.post(new FinalizeSpawnEvent((Mob) (Object) this, level, difficulty));
	}
}
