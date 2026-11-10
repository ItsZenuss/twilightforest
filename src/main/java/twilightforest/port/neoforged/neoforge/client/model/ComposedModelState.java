package twilightforest.port.neoforged.neoforge.client.model;

import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.core.Direction;
import org.joml.Matrix4fc;

public class ComposedModelState implements ModelState {
	private final ModelState first;
	private final Transformation second;

	public ComposedModelState(ModelState first, Transformation second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public Transformation transformation() {
		return first.transformation().compose(second);
	}

	@Override
	public Matrix4fc faceTransformation(Direction direction) {
		return first.faceTransformation(direction);
	}

	@Override
	public Matrix4fc inverseFaceTransformation(Direction direction) {
		return first.inverseFaceTransformation(direction);
	}
}
