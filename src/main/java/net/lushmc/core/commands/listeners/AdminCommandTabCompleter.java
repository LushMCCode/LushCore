package net.lushmc.core.commands.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class AdminCommandTabCompleter implements TabCompleter {

	Map<String, List<String>> cmds = new HashMap<>();

	public AdminCommandTabCompleter() {
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> completions = new ArrayList<>();
//		if (cmd.getName().equalsIgnoreCase("skull")) {
//			StringUtil.copyPartialMatches(args[0], SkullUtils.getSkullNames(), completions);
//		}

		return completions;

	}

	public List<String> getOnlinePlayers() {
		List<String> players = new ArrayList<>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			players.add(player.getName());
		}
		return players;
	}

}
