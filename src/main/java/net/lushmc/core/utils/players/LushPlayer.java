package net.lushmc.core.utils.players;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.json2.JSONArray;
import org.json2.JSONException;
import org.json2.JSONObject;

import net.lushmc.core.utils.CoreUtils;
import net.lushmc.core.utils.sql.SQLUtils;

public class LushPlayer {

	JSONObject data = new JSONObject("{}");

	LushPlayer(UUID uid) throws JSONException, SQLException {
		ResultSet rs = SQLUtils.getDatabase("core").query("SELECT * FROM player_data WHERE uuid='" + uid + "';");
		if (rs != null) {
			while (rs.next()) {
				Bukkit.broadcastMessage("Loaded from SQL");
				data.put("uuid", uid);
				data = new JSONObject(rs.getString("data"));
			}
		}
		if (!data.has("uuid")) {
			registerNewPlayer(uid);
		}
	}

	public void save() {

		Bukkit.broadcastMessage("Saving data to SQL");
		// Package the data

		data.put("lastUsername", Bukkit.getOfflinePlayer(UUID.fromString(data.getString("uuid"))).getName());
		if (!data.has("usernames")) {
			JSONArray array = new JSONArray();
			array.put(data.get("lastUsername"));
			data.put("usernames", array);
		} else {
			List<String> names = new ArrayList<>();
			for (Object o : data.getJSONArray("usernames"))
				names.add((String) o);
			if (!names.contains(data.getString("lastUsername")))
				data.getJSONArray("usernames").put(data.get("lastUsername"));
		}

		// Send the data

		if (SQLUtils.getDatabase("core")
				.update("UPDATE player_data SET data=\"" + CoreUtils.mysqlEscapeString(data.toString())
						+ "\" WHERE uuid=\"" + data.getString("uuid") + "\";") < 1) {
			try {
				Bukkit.broadcastMessage(SQLUtils.getDatabase("core")
						.input("INSERT INTO player_data(uuid, data) VALUES (\"" + data.getString("uuid") + "\", \""
								+ CoreUtils.mysqlEscapeString(data.toString()) + "\");")
						+ "");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void registerNewPlayer(UUID uid) {
		Bukkit.broadcastMessage("Registered new player");
		data.put("uuid", uid.toString());
		PlayerManager.registerPlayer(uid, this);
	}

}
