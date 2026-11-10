package twilightforest.port.neoforged.neoforge.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.HumanoidArm;
import twilightforest.port.neoforged.bus.api.Event;

public class RenderArmEvent extends Event {
	private final AbstractClientPlayer player;
	private final HumanoidArm arm;
	private final SubmitNodeCollector submitNodeCollector;
	private final PoseStack poseStack;
	private final int packedLight;

	public RenderArmEvent(AbstractClientPlayer player, HumanoidArm arm, SubmitNodeCollector submitNodeCollector, PoseStack poseStack, int packedLight) {
		this.player = player;
		this.arm = arm;
		this.submitNodeCollector = submitNodeCollector;
		this.poseStack = poseStack;
		this.packedLight = packedLight;
	}

	public AbstractClientPlayer getPlayer() {
		return player;
	}

	public HumanoidArm getArm() {
		return arm;
	}

	public SubmitNodeCollector getSubmitNodeCollector() {
		return submitNodeCollector;
	}

	public PoseStack getPoseStack() {
		return poseStack;
	}

	public int getPackedLight() {
		return packedLight;
	}
}
