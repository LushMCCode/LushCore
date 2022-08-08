package net.lushmc.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import net.lushmc.core.LushPlugin;
import net.lushmc.core.commands.listeners.PlayerCommandTabCompleter;
import net.lushmc.core.utils.CoreUtils;
import net.lushmc.core.utils.UID;
import net.lushmc.core.utils.announcements.AnnouncementUtils;

public class PlayerCommands implements CommandExecutor {

	public PlayerCommands(LushPlugin plugin, String... cmd) {
		for (String s : cmd) {
			PluginCommand com = plugin.getCommand(s);
			com.setExecutor(this);
			com.setTabCompleter(new PlayerCommandTabCompleter());
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("console") && sender instanceof Player) {
			if (args.length <= 1 || !AnnouncementUtils.hasClickId(UID.from(args[0]))) {
				sender.sendMessage(CoreUtils.prefixes("admin") + "Invalid cid.");
				return true;
			}
			String s = "";
			for (int i = 1; i != args.length; i++) {
				s = s.equals("") ? args[i] : s + " " + args[i];
			}
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(((Player) sender), s));
		}
		return true;
	}
}
