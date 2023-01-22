package net.lushmc.core.utils.players;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.json2.JSONException;
import org.json2.JSONObject;

import net.lushmc.core.utils.sql.SQLUtils;

public class LushPlayer {

	JSONObject data = new JSONObject("{}");

	LushPlayer(UUID uid) throws JSONException, SQLException {
		ResultSet rs = SQLUtils.getDatabase("core").query("SELECT * FROM player_data WHERE uuid='" + uid + "';");
		if (rs != null) {
			while (rs.next()) {
				data.put("uuid", uid);
				data.put("data", new JSONObject(rs.getString("data")));
			}
		}
		if (!data.has("uuid")) {
			registerNewPlayer(uid);
		}
	}

	public void save() {
		int r = SQLUtils.getDatabase("core").update(
				"UPDATE player_data SET data='" + data.toString() + "' WHERE uuid='" + data.getString("uuid") + "';");
		Bukkit.broadcastMessage("Result: " + r);
		if (r < 1) {
			String i = "INSERT INTO player_data(uuid, data) VALUES (\"" + data.getString("uuid") + "\", data=\""
					+ StringEscapeUtils.escapeJava(data.toString()) + "\");";
			Bukkit.broadcastMessage(i);
			Bukkit.broadcastMessage(SQLUtils.getDatabase("core").input(i) + "");
		}
	}

	private void registerNewPlayer(UUID uid) {
		data.put("uuid", uid.toString());
		PlayerManager.registerPlayer(uid, this);
	}

}
