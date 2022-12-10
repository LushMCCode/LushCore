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
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;

public class AdminCommandTabCompleter implements TabCompleter {

	Map<String, List<String>> cmds = new HashMap<>();

	public AdminCommandTabCompleter() {
		List<String> lsc = new ArrayList<>();
		lsc.add("update");
		lsc.add("reload");
		cmds.put("lush", lsc);

		List<String> rsc = new ArrayList<>();
		rsc.add("announcements");
		rsc.add("all");
		cmds.put("lush.reload", rsc);

		List<String> usc = new ArrayList<>();

		for (Plugin plugin : Bukkit.getPluginManager().getPlugins())
			if (plugin.getDescription().getAuthors().contains("QuickScythe"))
				usc.add(plugin.getName().replaceAll("_indev", ""));
		cmds.put("update", usc);

		List<String> pssc = new ArrayList<>();
		pssc.add("in-dev");
		pssc.add("official");
		cmds.put("update.status", pssc);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			if (cmd.getName().equalsIgnoreCase("update")) {
				StringUtil.copyPartialMatches(args[0], cmds.get("update"), completions);
			}
			if (cmd.getName().equalsIgnoreCase("lush")) {
				StringUtil.copyPartialMatches(args[0], cmds.get("lush"), completions);
			}
		}
		if (args.length == 2) {
			if (cmd.getName().equals("update")) {
				StringUtil.copyPartialMatches(args[1], cmds.get("update.status"), completions);
			}
			if (args[0].equalsIgnoreCase("reload")) {
				StringUtil.copyPartialMatches(args[1], cmds.get("lush.reload"), completions);
			}
		}

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
