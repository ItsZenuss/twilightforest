package twilightforest.port.neoforged.neoforge.common.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class FakePlayerFactory {
	private static final GameProfile MINECRAFT = new GameProfile(UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77"), "[Minecraft]");
	private static final Map<GameProfile, FakePlayer> FAKE_PLAYERS = new ConcurrentHashMap<>();

	private FakePlayerFactory() {}

	public static FakePlayer getMinecraft(ServerLevel level) {
		return get(level, MINECRAFT);
	}

	public static FakePlayer get(ServerLevel level, GameProfile profile) {
		return FAKE_PLAYERS.computeIfAbsent(profile, p -> new FakePlayer(level, p));
	}
}
