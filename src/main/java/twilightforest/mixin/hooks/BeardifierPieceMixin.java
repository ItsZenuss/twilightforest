package twilightforest.mixin.hooks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import twilightforest.port.neoforged.neoforge.common.world.PieceBeardifierModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Fabric port of NeoForge's PieceBeardifierModifier: non-jigsaw structure pieces can choose the box, terrain
 * adjustment and ground offset used when the terrain is adapted around them.
 * <p>
 * Vanilla ships without local variable tables, so the piece is captured from the {@code piece.getBoundingBox()}
 * call that feeds the non-jigsaw {@code new Rigid(...)} and handed to the constructor wrapper.
 */
@Mixin(Beardifier.class)
public abstract class BeardifierPieceMixin {
	@Unique
	private static final ThreadLocal<PieceBeardifierModifier> TF_CURRENT = new ThreadLocal<>();

	@WrapOperation(method = "forStructuresInChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/StructurePiece;getBoundingBox()Lnet/minecraft/world/level/levelgen/structure/BoundingBox;", ordinal = 1))
	private static BoundingBox tf$capturePiece(StructurePiece piece, Operation<BoundingBox> original) {
		if (piece instanceof PieceBeardifierModifier modifier) {
			TF_CURRENT.set(modifier);
			return modifier.getBeardifierBox();
		}
		TF_CURRENT.remove();
		return original.call(piece);
	}

	@WrapOperation(method = "forStructuresInChunk", at = @At(value = "NEW", target = "Lnet/minecraft/world/level/levelgen/Beardifier$Rigid;", ordinal = 1))
	private static Beardifier.Rigid tf$pieceModifier(BoundingBox box, TerrainAdjustment adjustment, int groundDelta, Operation<Beardifier.Rigid> original) {
		PieceBeardifierModifier modifier = TF_CURRENT.get();
		if (modifier != null) {
			TF_CURRENT.remove();
			return original.call(box, modifier.getTerrainAdjustment(), modifier.getGroundLevelDelta());
		}
		return original.call(box, adjustment, groundDelta);
	}
}
