package net.lushmc.core.utils.chat;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.lushmc.core.utils.CoreUtils;
import net.lushmc.core.utils.placeholders.PlaceholderUtils;

public class CoreChatUtils {

	private static Map<UUID, String> currentChannel = new HashMap<>();
	private static Map<String, Channel> channels = new HashMap<>();

	private static String playerformat = "%player: %message";
	private static String staffformat = "&c[StaffChat]&f%player: %message";
	private static String channelformat = "&a[%channel]&f%player: %message";

	public static void start() {
		addChannel("default", "", false);
		addChannel("staff", "&7[&3&lStaff&7] ", true);
		addChannel("zachs", "&7[&c&lzACHS&7] ", true);
		addChannel("punish", "&7[&6&lPunISH&7] ", true);
		addChannel("vip", "&7[&a&lVIP&7] ", false);
		registerChannels();

	}

	public static String fade(String fromHex, String toHex, String string) {
		int[] start = getRGB(fromHex);
		int[] last = getRGB(toHex);

		StringBuilder sb = new StringBuilder();

		Integer dR = numberFade(start[0], last[0], string.length());
		Integer dG = numberFade(start[1], last[1], string.length());
		Integer dB = numberFade(start[2], last[2], string.length());

		for (int i = 0; i < string.length(); i++) {
			Color c = new Color(start[0] + dR * i, start[1] + dG * i, start[2] + dB * i);

			sb.append(net.md_5.bungee.api.ChatColor.of(c) + "" + string.charAt(i));
		}
		return sb.toString();
	}

	private static int[] getRGB(String rgb) {
		int[] ret = new int[3];
		for (int i = 0; i < 3; i++) {
			ret[i] = hexToInt(rgb.charAt(i * 2), rgb.charAt(i * 2 + 1));
		}
		return ret;
	}

	private static int hexToInt(char a, char b) {
		int x = a < 65 ? a - 48 : a - 55;
		int y = b < 65 ? b - 48 : b - 55;
		return x * 16 + y;
	}

	private static Integer numberFade(int i, int f, int n) {
		int d = (f - i) / (n - 1);
		return d;
	}

	public static String getPlayerFormat() {
		return playerformat;
	}

	public static String getStaffFormat() {
		return staffformat;
	}

	public static String getChannelFormat(String channel) {
		return channelformat.replaceAll("%channel",
				channel.substring(0, 1).toUpperCase() + channel.substring(1, channel.length()).toLowerCase());
	}

	public static Channel addChannel(String name, String tag, boolean global) {
		return channels.put(name, createChannel(name, tag, global));
	}

	public static Channel addChannel(String name, Channel channel) {
		return channels.put(name, channel);
	}

	public static Channel createChannel(String name, String tag, boolean global, boolean local, int distance) {
		return new Channel() {

			@Override
			public boolean isGlobal() {
				return global;
			}

			@Override
			public String getTag() {
				return tag;
			}

			@Override
			public String getName() {
				return name;
			}

			@Override
			public boolean isLocal() {
				return local;
			}

			@Override
			public int getDistance() {
				return distance;
			}
		};
	}

	public static void removeChannel(String channel) {
		channels.remove(channel);
	}

	public static Channel createChannel(String name, String tag, boolean global) {
		return createChannel(name, tag, global, false, -1);
	}

	public static void registerChannels() {
		try {

			FileConfiguration conf = CoreUtils.getPlugin().getConfig();
			for (String idstr : conf.getConfigurationSection("Channels").getKeys(false)) {
				addChannel(idstr, conf.getString("Channels." + idstr + ".tag", "&a&l[%channel]&7 "),
						conf.getBoolean("Channels." + idstr + ".global", false));

			}

		} catch (NullPointerException ex) {

		}
	}

	public static String getChannel(UUID uid) {
		return currentChannel.containsKey(uid) ? currentChannel.get(uid) : "default";
	}

	public static void sendBroadcast(String message) {
		CoreUtils.sendPluginMessage((Player) Bukkit.getOnlinePlayers().toArray()[0], "mystic:mystic",
				"MysticChat-broadcast", message);
	}

	public static void sendChannelChat(Player player, String channel, String message) {

		String format;
		switch (channel) {
		case "staff":
			format = getStaffFormat();
			break;
		case "default":
			format = getPlayerFormat();
			break;
		case "zachs":
			format = "&f%message";
			break;
		default:
			format = getChannelFormat(channel);
			break;
		}
		Channel ch = getChannel(channel);
		format = CoreUtils.colorize(getChannel(channel).getTag() + "&7") + format;
		if (ch.isGlobal())
			CoreUtils.sendPluginMessage(player, "mystic:mystic", "MysticChat-" + channel,
					replaceholders(player, format, message));
		else if (ch.isLocal()) {
			for (Player pl : player.getWorld().getPlayers()) {
				if (player.getLocation().distance(pl.getLocation()) <= ch.getDistance() || ch.getDistance() == -1)
					pl.sendMessage((replaceholders(player, format, message)));
			}
		} else {
			for (Player s : Bukkit.getOnlinePlayers()) {
				if (ch.getDistance() == -1 || s.hasPermission("mysticcloud.chat." + channel.toLowerCase()))
					s.sendMessage((replaceholders(player, format, message)));
			}
		}
	}

//	public static void sendChannelChat(Player player, String channel, String message) {
//
//		String format;
//		switch (channel) {
//		case "staff":
//			format = getStaffFormat();
//			break;
//		case "default":
//			format = getPlayerFormat();
//			break;
//		default:
//			format = getChannelFormat(channel);
//			break;
//		}
//		format = CoreUtils.colorize(getChannel(channel).getTag() + "&7") + format;
//		if (getChannel(channel).isGlobal())
//			CoreUtils.sendPluginMessage(player, "mystic:mystic", "MysticChat-" + channel,
//					replaceholders("&6CONSOLE", format, message));
//		else if (getChannel(channel).isLocal()) {
//			if (getChannel(channel).getDistance() == -1) {
//				for(Player pl : player.getWorld().getPlayers()) {
//					pl.sendMessage((replaceholders("&6CONSOLE", format, message)));
//				}
//			}
//		} else {
//			for (Player s : Bukkit.getOnlinePlayers()) {
//				if (s.hasPermission("mysticcloud.chat." + channel.toLowerCase()))
//					s.sendMessage((replaceholders("&6CONSOLE", format, message)));
//			}
//		}
//	}

	public static String censor(String message) {
//		String s = "";
//		String original = message;
//		int i = message.length();
//		for (int l = 0; l != i; l++) {
//			try {
//
//				if (message.substring(l, l + 1).equalsIgnoreCase(message.substring(l - 2, l - 1))
//						&& message.substring(l, l + 1).equalsIgnoreCase(message.substring(l - 1, l)))
//					continue;
//				s = s + (l == 0 ? message.substring(l, l + 1).toUpperCase() : message.substring(l, l + 1));
//				// Curses "try"
//				try {
//					for (CharSequence[] curses : replacements.keySet()) {
//						for (CharSequence curse : curses) {
//							if (s.length() < curse.length())
//								continue;
//
//							if (s.substring(s.length() - (curse.length()), s.length()).toLowerCase().contains(curse)) {
//								s = s.substring(0, s.length() - (curse.length()));
//								s = s + replacements.get(curses);
//								i = i + ((curse.length() - replacements.get(curses).length())); // Temp code. Should set
//																								// i to i +
//								// (difference in length in curse
//								// and
//								// replacement) but the try catch
//								// under the first loop catches this
//								// mistake for
//								// me.
//								original = original.replace(curse, replacements.get(curses));
//							}
//						}
//					}
//				} catch (IndexOutOfBoundsException ex) {
//					continue;
//				}
//
//			} catch (IndexOutOfBoundsException ex) {
//				if ((l + 1) < message.length())
//					s = s + (l == 0 ? message.substring(l, l + 1).toUpperCase() : message.substring(l, l + 1));
//			}
//
//		}
//		boolean tmp = false;
//		for (String p : puncuation)
//			if (s.endsWith(p)) {
//				tmp = true;
//				break;
//			}
//
//		if (!tmp)
//			s = s + ".";
//
//		return s;
		return message;
	}

	public static String replaceholders(Player player, String format, String message) {
		format = CoreUtils.colorize(format);
		message = censor(message);

		if (format.contains("%message"))
			format = format.replace("%message",
					player.hasPermission("mysticcloud.chat.color")
							? CoreUtils.colorize(PlaceholderUtils.emotify(message))
							: message);
		format = PlaceholderUtils.replace(player, format);
		return format;
	}

	public static String replaceholders(String player, String format, String message) {
		format = CoreUtils.colorize(format);
		message = censor(message);

		if (format.contains("%message"))
			format = format.replace("%message", CoreUtils.colorize(PlaceholderUtils.emotify(message)));
		format = PlaceholderUtils.replace(player, format);

		return format;
	}

	public static Channel getChannel(String name) {
		return channels.containsKey(name) ? channels.get(name) : channels.get("default");
	}

	public static void sendMessage(Player player, String message) {
		sendChannelChat(player, getChannel(player.getUniqueId()), message);
	}

	public static void setChannel(Player player, String channel, boolean message) {
		if ((channel.equalsIgnoreCase("zachs") || channel.equalsIgnoreCase("punish"))
				&& !player.getUniqueId().toString().equals("60191757-427b-421e-bee0-399465d7e852")) {
			return;
		}
		if (channels.containsKey(channel.toLowerCase())) {
			if (player.hasPermission("mysticcloud.channel." + channel.toLowerCase())) {
				currentChannel.put(player.getUniqueId(), channel);
				if (message)
					player.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("chat") + "You're now chatting in the &7"
							+ channel.toLowerCase() + "&f channel."));
			} else {
				if (message)
					player.sendMessage(
							CoreUtils.prefixes("chat") + "Sorry, you don't have permission to join that channel.");
			}
		} else {
			if (message)
				player.sendMessage(CoreUtils.prefixes("chat") + "Sorry that channel doesn't exist.");
		}
	}

	public static void setChannel(Player player, String channel) {
		setChannel(player, channel, true);
	}

}
