package net.lushmc.core.utils.announcements;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.json2.JSONObject;

import me.clip.placeholderapi.PlaceholderAPI;
import net.lushmc.core.utils.CoreUtils;
import net.lushmc.core.utils.UID;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class Announcement {

	String name;
	Sound sound;
	boolean enabled;

	List<String> announcement = new ArrayList<>();

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

	private Announcement setAnnouncementString(String... strings) {
		List<String> l = new ArrayList<>();
		for (String s : strings)
			l.add(s);
		return setAnnouncementList(l);
	}

	private Announcement setAnnouncementList(List<String> announcement) {
		this.announcement = announcement;
		return this;
	}

	@SuppressWarnings("unchecked")
	public Announcement setAnnouncement(Object strings) {
		return strings instanceof List ? setAnnouncementList((List<String>) strings)
				: setAnnouncementString(strings + "");
	}

	public Announcement announce(Player player) {
		for (String line : announcement) {
			ComponentBuilder builder = new ComponentBuilder();
			if (line.contains("{") && line.contains("}")) {
				int open = 0;
				int close = 0;
				LinkedList<String> jsono = new LinkedList<>();
				String s = "";
				for (int c = 0; c != line.length(); c++) {
					if (line.substring(c, c + 1).equals("{"))
						open = open + 1;
					if (open >= 1) {
						if (line.substring(c, c + 1).equals("}"))
							close = close + 1;
						s = s + line.substring(c, c + 1);
						if (open == close) {
							jsono.add(s);
							s = "";
							open = 0;
							close = 0;
						}
					}
				}

				for (String j : jsono) {
					line = line.replace(j, "%-SPLIT-%");
				}
				int i = 0;
				for (String a : line.split("%-SPLIT-%")) {
					TextComponent l = new TextComponent(PlaceholderAPI.setPlaceholders(player, CoreUtils.colorize(a)));
					l.setClickEvent(null);
					l.setHoverEvent(null);
					builder = builder.append(l);
					Bukkit.broadcastMessage("append 1: " + l);
					if (line.endsWith(a)) {
						Bukkit.broadcastMessage("break: " + a);
						break;
					}
					JSONObject json = new JSONObject(jsono.get(i));
					TextComponent sb = new TextComponent(CoreUtils.colorize(json.getString("text")));
					Bukkit.broadcastMessage("append 2: " + json.getString("text"));
					if (json.has("cmd"))
						sb.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
								PlaceholderAPI.setPlaceholders(player, json.getString("cmd"))));
					if (json.has("hover"))
						sb.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
								CoreUtils.colorize(PlaceholderAPI.setPlaceholders(player, json.getString("hover"))))));
					if (json.has("https"))
						sb.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
								PlaceholderAPI.setPlaceholders(player, json.getString("https"))));
					if (json.has("console")) {
						sb.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/console "
								+ AnnouncementUtils.createClickID().toString() + " " + json.getString("console")));
					}

					Bukkit.broadcastMessage("append 3");
					builder = builder.append(sb);
					i = i + 1;
				}
			} else
				builder = builder.append(CoreUtils.colorize(PlaceholderAPI.setPlaceholders(player, line)));
			player.spigot().sendMessage(builder.create());
		}
		if (sound != null)
			player.playSound(player.getLocation(), sound, 10, 0);

		return this;
	}

	public boolean isEnabled() {
		return enabled;
	}

}
