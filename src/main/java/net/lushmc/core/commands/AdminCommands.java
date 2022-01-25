package net.lushmc.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import net.lushmc.core.LushPlugin;
import net.lushmc.core.commands.listeners.AdminCommandTabCompleter;
import net.lushmc.core.utils.CoreUtils;

public class AdminCommands implements CommandExecutor {

	public AdminCommands(LushPlugin plugin, String... cmd) {
		for (String s : cmd) {
			PluginCommand com = plugin.getCommand(s);
			com.setExecutor(this);
			com.setTabCompleter(new AdminCommandTabCompleter());
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("lush")) {
			if (!sender.hasPermission("lush.admin")) {
				sender.sendMessage(
						CoreUtils.prefixes("admin") + CoreUtils.colorize("You don't have permission for that."));
				return true;
			}
			if (args.length == 0) {
				return true;
			}
			if (args[0].equalsIgnoreCase("update")) {
				String plugin = args[0];
				String filename = plugin + ".jar";
				String url = "https://jenkins.mysticcloud.net/job/" + plugin
						+ "/job/master/lastSuccessfulBuild/artifact/target/" + filename;
				sender.sendMessage(CoreUtils.prefixes("admin") + "Downloading " + filename + "...");
				if (CoreUtils.downloadFile(url, "plugins/" + filename, "admin", "BAG3jbe!Q#C7XaYJ"))
					sender.sendMessage(
							CoreUtils.prefixes("admin") + CoreUtils.colorize("Finished downloading " + filename));
				else {
					sender.sendMessage(CoreUtils.prefixes("admin") + CoreUtils
							.colorize("There was an error downloading " + filename + ". Trying alt site..."));
					if (CoreUtils.downloadFile("https://downloads.mysticcloud.net/" + filename, "plugins/" + filename,
							"admin", "v4pob8LW"))
						sender.sendMessage(
								CoreUtils.prefixes("admin") + CoreUtils.colorize("Finished downloading " + filename));
					else {
						sender.sendMessage(CoreUtils.prefixes("admin") + CoreUtils
								.colorize("There was an error downloading " + filename + ". Trying alt site..."));
					}
				}
			}
		}
		return true;
	}
}
