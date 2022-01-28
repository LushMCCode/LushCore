package net.lushmc.core.utils.announcements;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.json2.JSONObject;

public class Announcement {

	String name;
	Sound sound;
	boolean enabled;

	List<String> annoucement = new ArrayList<>();

	public Announcement(String name) {
		this.name = name;
		sound = null;
		enabled = false;
	}

	public Announcement setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public Announcement setSound(Sound sound) {
		this.sound = sound;
		return this;
	}

	private Announcement setAnnoucementString(String... strings) {
		List<String> l = new ArrayList<>();
		for (String s : strings)
			l.add(s);
		return setAnnoucement(l);
	}

	private Announcement setAnnoucementList(List<String> annoucement) {
		this.annoucement = annoucement;
		return this;
	}

	@SuppressWarnings("unchecked")
	public Announcement setAnnoucement(Object strings) {
		return strings instanceof List ? setAnnoucementList((List<String>) strings)
				: setAnnoucementString(strings + "");
	}

	public Announcement announce(Player player) {
		for (String line : annoucement) {
			Bukkit.broadcastMessage(line);
			if (line.contains("{") && line.contains("}")) {
				int open = 0;
				int close = 0;
				LinkedList<JSONObject> jsono = new LinkedList<>();
				String s = "";
				for (int c = 0; c != line.length(); c++) {
					if (line.substring(c, c + 1).equals("{"))
						open = open + 1;
					if (open >= 1) {
						if (line.substring(c, c + 1).equals("}"))
							close = close + 1;
						s = s + line.substring(c, c + 1);

						if (open == close) {
							jsono.add(new JSONObject(s));
							s = "";
							open = 0;
							close = 0;
						}

					}
				}
				int i = 0;
				for (JSONObject json : jsono) {
					line = line.replace(json.toString(), "%-" + i + "-%");
//					BaseComponent[] accept = new ComponentBuilder("Type ").color(ChatColor.WHITE).append("/tpaccept")
//							.color(ChatColor.GRAY).append(" or click ").color(ChatColor.WHITE).append("[Accept]")
//							.color(ChatColor.GREEN).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"))
//							.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
//									new Text(CoreUtils.colorize(
//											"&fClick to &aaccept&f the\n&fteleport request from\n&7" + player.getName()))))
//							.create();

					i = i + 1;
				}
				for (String a : line.split("[0-9]")) {
					Bukkit.broadcastMessage(a);
				}

			}
		}
		return this;
	}

}
