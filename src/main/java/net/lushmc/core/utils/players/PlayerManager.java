package net.lushmc.core.utils.players;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json2.JSONException;

public class PlayerManager {

	private static Map<UUID, LushPlayer> players = new HashMap<>();

	public static LushPlayer getPlayer(UUID uid) {
		try {
			return players.containsKey(uid) ? players.get(uid) : new LushPlayer(uid);
		} catch (JSONException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	static void registerPlayer(UUID uid, LushPlayer lushPlayer) {
		players.put(uid, lushPlayer);
	}

}
