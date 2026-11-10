package twilightforest.events;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import twilightforest.port.neoforged.bus.api.SubscribeEvent;
import twilightforest.port.neoforged.neoforge.common.NeoForge;
import twilightforest.port.neoforged.neoforge.event.entity.EntityMountEvent;
import twilightforest.port.neoforged.neoforge.event.entity.EntityTeleportEvent;
import twilightforest.port.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import twilightforest.port.neoforged.neoforge.event.entity.player.PlayerEvent;
import twilightforest.port.neoforged.neoforge.event.tick.EntityTickEvent;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import twilightforest.entity.IHostileMount;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFDataAttachments;

@Component
public class HostileMountEvents {

	public static volatile boolean allowDismount = false;

	@PostConstruct
	private void setup() {
		NeoForge.EVENT_BUS.addListener(LivingIncomingDamageEvent.class, this::handleMountDamage);
		NeoForge.EVENT_BUS.addListener(EntityTeleportEvent.class, this::preventTeleportingOffHostileMounts);
		NeoForge.EVENT_BUS.addListener(EntityMountEvent.class, this::preventMountDismount);
		NeoForge.EVENT_BUS.addListener(EntityTickEvent.Post.class, this::preventHostilMountCrouching);
		NeoForge.EVENT_BUS.addListener(PlayerEvent.PlayerLoggedOutEvent.class, this::playerLogOut);
	}

	@SubscribeEvent
	private void handleMountDamage(LivingIncomingDamageEvent event) {
		LivingEntity living = event.getEntity();
		DamageSource damageSource = event.getSource();
		// lets not make the player take suffocation damage if riding something
		if (living instanceof Player && isRidingUnfriendly(living) && damageSource.is(DamageTypes.IN_WALL)) {
			event.setCanceled(true);
		}

		if (damageSource.is(DamageTypes.FALL) && living.getData(TFDataAttachments.YETI_THROWING).getThrown()) {
			float amount = event.getAmount();
			event.setCanceled(true);
			living.hurt(TFDamageTypes.getEntityDamageSource(living.level(), TFDamageTypes.YEETED, living.getData(TFDataAttachments.YETI_THROWING).getThrower()), amount);
		}
	}

	@SubscribeEvent
	private void playerLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
			hostileDismount(event.getEntity());
	}

	private void preventTeleportingOffHostileMounts(EntityTeleportEvent event) {
		// if our grabbed target tries to teleport dont let them
		if (event.getEntity() instanceof LivingEntity living && isRidingUnfriendly(living)) {
			event.setCanceled(true);
		}
	}

	public static void hostileDismount(Entity rider) {
		HostileMountEvents.allowDismount = true;
		rider.stopRiding();
		HostileMountEvents.allowDismount = false;
	}

	private void preventMountDismount(EntityMountEvent event) {
		if (!event.getLevel().isClientSide() &&
			!event.isMounting() && event.getEntityBeingMounted().isAlive() &&
			event.getEntityMounting() instanceof Player player && player.isAlive() &&
			isRidingUnfriendly(player) && !allowDismount && !player.getAbilities().invulnerable)
			event.setCanceled(true);
	}

	private void preventHostilMountCrouching(EntityTickEvent.Post event) {
		if (event.getEntity() instanceof IHostileMount)
			event.getEntity().getPassengers().forEach(e -> e.setShiftKeyDown(false));
	}

	public static boolean isRidingUnfriendly(LivingEntity entity) {
		return entity.isPassenger() && entity.getVehicle() instanceof IHostileMount;
	}
}
