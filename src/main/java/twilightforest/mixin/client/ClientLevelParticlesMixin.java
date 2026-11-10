package twilightforest.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import twilightforest.port.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;
import twilightforest.port.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fabric port: IClientBlockExtensions#addHitEffects / #addDestroyEffects for custom block break particles.
 */
@Mixin(ClientLevel.class)
public abstract class ClientLevelParticlesMixin {
	@Inject(method = "addDestroyBlockEffect", at = @At("HEAD"), cancellable = true)
	private void tf$destroyEffects(BlockPos pos, BlockState state, CallbackInfo ci) {
		IClientBlockExtensions ext = RegisterClientExtensionsEvent.getBlockExtensions(state.getBlock());
		if (ext != null && ext.addDestroyEffects(state, (ClientLevel) (Object) this, pos, Minecraft.getInstance().particleEngine)) {
			ci.cancel();
		}
	}

	@Inject(method = "addBreakingBlockEffect", at = @At("HEAD"), cancellable = true)
	private void tf$hitEffects(BlockPos pos, Direction side, CallbackInfo ci) {
		ClientLevel self = (ClientLevel) (Object) this;
		BlockState state = self.getBlockState(pos);
		IClientBlockExtensions ext = RegisterClientExtensionsEvent.getBlockExtensions(state.getBlock());
		if (ext != null && ext.addHitEffects(state, self, new BlockHitResult(Vec3.atCenterOf(pos), side, pos, false), Minecraft.getInstance().particleEngine)) {
			ci.cancel();
		}
	}
}
