package twilightforest.port.neoforged.neoforge.event.entity;

import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;
import twilightforest.port.neoforged.bus.api.ICancellableEvent;

public class ProjectileImpactEvent extends EntityEvent implements ICancellableEvent {
	private final Projectile projectile;
	private final HitResult rayTraceResult;
	private boolean canceled;

	public ProjectileImpactEvent(Projectile projectile, HitResult rayTraceResult) {
		super(projectile);
		this.projectile = projectile;
		this.rayTraceResult = rayTraceResult;
	}

	public Projectile getProjectile() {
		return projectile;
	}

	public HitResult getRayTraceResult() {
		return rayTraceResult;
	}

	@Override
	public boolean isCanceled() {
		return canceled;
	}

	@Override
	public void setCanceled(boolean cancel) {
		this.canceled = cancel;
	}
}
