package net.lushmc.core.commands;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.FileUtil;

import net.lushmc.core.LushPlugin;
import net.lushmc.core.commands.listeners.AdminCommandTabCompleter;
import net.lushmc.core.utils.CoreUtils;
import net.lushmc.core.utils.admin.DebugUtils;
import net.lushmc.core.utils.announcements.AnnouncementUtils;
import net.lushmc.core.utils.placeholders.EmoticonType;
import net.lushmc.core.utils.placeholders.Emoticons;

public class AdminCommands implements CommandExecutor {

	public AdminCommands(LushPlugin plugin, String... cmd) {
		for (String s : cmd) {
			PluginCommand com = plugin.getCommand(s);
			com.setExecutor(this);
			com.setTabCompleter(new AdminCommandTabCompleter());
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("update")) {
			String plugin = args.length == 0 ? "lush-core" : args[0];
			String status = args.length < 2 ? "official" : args[1];
			String format = plugin + "(" + status + ")";

			sender.sendMessage(CoreUtils.prefixes("admin") + "Updating " + format + "...");
			if (CoreUtils.updatePlugin(plugin, status))
				sender.sendMessage(
						CoreUtils.prefixes("admin") + CoreUtils.colorize("Finished updating " + format + "."));
			else
				sender.sendMessage(CoreUtils.prefixes("admin")
						+ CoreUtils.colorize("There was an error downloading " + format + "."));
		}

		if (cmd.getName().equalsIgnoreCase("lush")) {
			if (!sender.hasPermission("lush.admin")) {
				sender.sendMessage(
						CoreUtils.prefixes("admin") + CoreUtils.colorize("You don't have permission for that."));
				return true;
			}
			if (args.length == 0) {
				return true;
			}
			if (args[0].equalsIgnoreCase("reload")) {
				if (args.length == 1) {
					sender.sendMessage(CoreUtils.prefixes("admin")
							+ CoreUtils.colorize("Usage: /lush reload <all,announcements>"));
				} else {
					switch (args[1].toLowerCase()) {
					case "announcements":
						sender.sendMessage(CoreUtils.prefixes("admin") + "Reloading Announcements.");
						AnnouncementUtils.reload();
						break;
					case "all":
						sender.sendMessage(CoreUtils.prefixes("admin") + "Reloading all configuration files.");
						CoreUtils.reload();
						break;
					default:
						sender.sendMessage(CoreUtils.colorize(CoreUtils.prefixes("admin") + "Invalid System."));
						break;
					}
				}
			}
			if (args[0].equals("pull")) {
				// `/lush pull lush_guis <indev>
				if (args.length < 2) {
					sender.sendMessage(CoreUtils
							.colorize(CoreUtils.prefixes("admin") + "Usage: /lush pull <plugin>"/* " [status] */));
					return true;
				}
				Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(args[1]);
				String status = args.length == 3 ? args[2] : "indev";
				File other = new File(plugin.getDataFolder().getParentFile() + "/" + plugin.getName() + "_" + status);
				for (File file : other.listFiles())
					FileUtil.copy(file, new File(plugin.getDataFolder() + "/" + file.getName()));
				sender.sendMessage(
						CoreUtils.prefixes("admin") + "Data pull complete. Please reload/restart all systems.");

			}
		}
		if (cmd.getName().equalsIgnoreCase("debug")) {
			if (sender instanceof Player) {
				if (sender.hasPermission("lushmc.debug")) {
					if (args.length == 0) {
						if (DebugUtils.isDebugger(((Player) sender).getUniqueId())) {
							sender.sendMessage(CoreUtils.prefixes("debug") + "Removing you from the Debug Group");
							DebugUtils.removeDebugger(((Player) sender).getUniqueId());
							return true;
						}
						sender.sendMessage(CoreUtils.prefixes("debug") + "Adding you to the Debug Group");
						DebugUtils.addDebugger(((Player) sender).getUniqueId());
						return true;

					}

					if (args.length >= 1) {
						if (args[0].equalsIgnoreCase("emoticons")) {
							if (args.length == 1) {
								for (Emoticons emote : Emoticons.values())
									sender.sendMessage(emote.name() + ": " + emote);

							}
							if (args.length == 2) {
								for (Emoticons emote : Emoticons.values()) {
									if (EmoticonType.valueOf(args[1].toUpperCase()) != null) {
										if (emote.getTypes().contains(EmoticonType.valueOf(args[1].toUpperCase()))) {
											sender.sendMessage(emote.name() + ": " + emote);
										}
									} else if (emote.name().contains(args[1].toUpperCase()))
										sender.sendMessage(emote.name() + ": " + emote);
								}
							}
						}
						if (args.length == 2) {
							if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")
									|| args[0].equalsIgnoreCase("add")) {
								if (Bukkit.getPlayer(args[1]) == null) {
									sender.sendMessage(CoreUtils.prefixes("debug") + "That player is not online.");
									return true;
								}
								if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")) {
									UUID uid = Bukkit.getPlayer(args[1]).getUniqueId();
									if (DebugUtils.isDebugger(uid)) {
										DebugUtils.removeDebugger(uid);
										Bukkit.getPlayer(uid).sendMessage(
												CoreUtils.prefixes("debug") + "Removing you from the Debug Group");
									} else
										sender.sendMessage(
												CoreUtils.prefixes("debug") + "That player isn't in the Debug Group");

									return true;
								}
								if (args[0].equalsIgnoreCase("add")) {
									UUID uid = Bukkit.getPlayer(args[1]).getUniqueId();
									if (!DebugUtils.isDebugger(uid)) {
										DebugUtils.addDebugger(uid);
										Bukkit.getPlayer(uid).sendMessage(
												CoreUtils.prefixes("debug") + "Adding you to the Debug Group");
									} else
										sender.sendMessage(CoreUtils.prefixes("debug")
												+ "That player is already in the Debug Group");

									return true;
								}
							}
						}
					}
				}
			}
			return true;
		}
		return true;
	}
}
