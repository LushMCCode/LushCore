package net.lushmc.core.utils.placeholders;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.lushmc.core.utils.CoreUtils;
import net.lushmc.core.utils.chat.CoreChatUtils;

public class PlaceholderUtils {

	static Map<String, PlaceholderWorker> placeholders = new HashMap<>();

	public static void registerPlaceholders() {

		PlaceholderWorker name = new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				return player.getName();
			}
		};

//		PlaceholderWorker rank = new PlaceholderWorker() {
//
//			@Override
//			public String run(Player player) {
//				return CoreUtils.colorize("" + CoreUtils.getPlayerPrefix(player));
//			}
//		};
//		placeholders.put("%suffix%", new PlaceholderWorker() {
//
//			@Override
//			public String run(Player player) {
//				return CoreUtils.colorize(CoreUtils.getPlayerSuffix(player));
//			}
//
//		});
		PlaceholderWorker dname = new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				return player.getDisplayName();
			}
		};
		PlaceholderWorker cname = new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				return player.getCustomName();
			}
		};

		placeholders.put("%world%", new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				return player.getWorld().getName();
			}
		});
		placeholders.put("%playertime%", new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				return "" + player.getPlayerTime();
			}

		});
		
		placeholders.put("%online%", new PlaceholderWorker() {

			@Override
			public String run(Player player) {
				return Bukkit.getOnlinePlayers().size() + "";
			}

		});

		placeholders.put("%displayname%", dname);
		placeholders.put("%dname%", dname);

		placeholders.put("%customname%", cname);
		placeholders.put("%cname%", cname);

		placeholders.put("%player%", name);
		placeholders.put("%pl%", name);

//		placeholders.put("%r%", rank);
//		placeholders.put("%rank%", rank);
//		placeholders.put("%prefix%", rank);

	}

	public static void registerPlaceholder(String key, PlaceholderWorker worker) {
		placeholders.put("%" + key + "%", worker);
	}

	public static String replace(Player player, String string) {

		for (Entry<String, PlaceholderWorker> e : placeholders.entrySet()) {
			if (string.contains(e.getKey())) {
				string = string.replaceAll(e.getKey(), e.getValue().run(player));
			}
		}

		string = emotify(string);

		return string;
	}

	public static String replace(String player, String string) {
		if (string.contains("%lvl"))
			string = string.replaceAll("%lvl", "");

		string = string.replaceAll("%player", player);
		string = string.replaceAll("%pl", player);
		string = string.replaceAll("%world", "");
		string = string.replaceAll("%balance", "");
		string = string.replaceAll("%gems", "");
		string = string.replaceAll("%g", "");
		string = string.replaceAll("%level", "");
		string = string.replaceAll("%rank", "%r%");
		string = string.replaceAll("%prefix", "%r%");
		string = string.replaceAll("%r%", "");
		string = string.replaceAll("%displayname", "");
		string = string.replaceAll("%customname", "");
		string = string.replaceAll("%time", "");
		string = string.replaceAll("%playertime", "");
		string = string.replaceAll("%suffix", "");

		if (string.contains("%tag"))
			string = string.replaceAll("%tag", "");
		if (string.contains("%nitro"))
			string = string.replace("%nitro", "");
		string = emotify(string);

		return CoreUtils.colorize(string);
	}

	public static String emotify(String string) {
		String tag = string + "";
		while (tag.contains("%emoticon:")) {
			String icon = tag.split("moticon:")[1].split("%")[0];
			if (Emoticons.valueOf(icon.toUpperCase()) == null) {
				tag = tag.replaceAll("%emoticon:" + icon + "%", Emoticons.UNKNOWN.toString());
			} else {
				tag = tag.replaceAll("%emoticon:" + icon + "%", Emoticons.valueOf(icon.toUpperCase()).toString());
			}
		}
		return tag;
	}

	public static String markup(String string) {
		String tag = string + "";
		while (tag.contains("%bold:")) {
			String icon = tag.split("old:")[1].split("%")[0];
			tag = tag.replaceAll("%bold:" + icon + "%",
					ChatColor.BOLD + icon + ChatColor.getLastColors(tag.split("%bold")[0]));
		}
		while (tag.contains("%upper:")) {
			String icon = tag.split("pper:")[1].split("%")[0];
			tag = tag.replaceAll("%upper:" + icon + "%",
					icon.contains("%") ? replace("", icon).toUpperCase() : icon.toUpperCase());
		}
		while (tag.contains("%fade:")) {
			String from = tag.split(":")[1];
			String to = tag.split(":")[2];
			String s = tag.split(":")[3].split("%")[0];

			tag = tag.replaceFirst("%fade:" + from + ":" + to + ":" + s + "%", CoreChatUtils.fade(from, to, s));
		}
		return tag;
	}

}
