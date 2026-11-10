package twilightforest.fabric;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import twilightforest.port.neoforged.neoforge.entity.PartEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Fabric port of NeoForge's multipart entity support. NeoForge patched {@code Level#getEntities} to include the
 * {@link PartEntity}s of multipart entities (the same way vanilla includes ender dragon parts) and resolves part ids
 * for interaction packets. This tracks loaded multipart entities per level so the level mixins can do the same.
 */
public final class MultipartEntities {
	private static final Map<Level, Set<Entity>> TRACKED = Collections.synchronizedMap(new WeakHashMap<>());

	private MultipartEntities() {}

	public static void onLoad(Entity entity, Level level) {
		if (entity.isMultipartEntity()) {
			TRACKED.computeIfAbsent(level, l -> Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()))).add(entity);
		}
	}

	public static void onUnload(Entity entity, Level level) {
		Set<Entity> set = TRACKED.get(level);
		if (set != null) set.remove(entity);
	}

	private static Entity[] snapshot(Level level) {
		Set<Entity> set = TRACKED.get(level);
		if (set == null || set.isEmpty()) return new Entity[0];
		synchronized (set) {
			return set.toArray(new Entity[0]);
		}
	}

	/**
	 * Adds every part of a tracked multipart entity whose box intersects {@code area} and that {@code mapper} accepts.
	 */
	public static <T> void collectParts(Level level, @Nullable Entity except, AABB area, Function<Entity, T> mapper, Predicate<? super T> filter, List<? super T> out) {
		for (Entity holder : snapshot(level)) {
			PartEntity<?>[] parts = holder.getParts();
			if (parts == null) continue;
			for (PartEntity<?> part : parts) {
				if (part == except || !part.getBoundingBox().intersects(area)) continue;
				T mapped = mapper.apply(part);
				if (mapped != null && filter.test(mapped)) out.add(mapped);
			}
		}
	}

	@Nullable
	public static Entity findPart(Level level, int id) {
		for (Entity holder : snapshot(level)) {
			PartEntity<?>[] parts = holder.getParts();
			if (parts == null) continue;
			for (PartEntity<?> part : parts) {
				if (part.getId() == id) return part;
			}
		}
		return null;
	}
}
