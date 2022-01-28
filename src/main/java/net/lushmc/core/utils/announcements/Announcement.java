package net.lushmc.core.utils.announcements;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.json2.JSONObject;

import me.clip.placeholderapi.PlaceholderAPI;
import net.lushmc.core.utils.CoreUtils;
import net.lushmc.core.utils.UID;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
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
		return setAnnouncement(l);
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
					i = i + 1;
				}
				i = 0;
				for (String a : line.split("%-[0-9]-%")) {
					builder.append(CoreUtils.colorize(a));
					if (line.endsWith(a))
						break;
					JSONObject json = jsono.get(i);
					builder.append(CoreUtils.colorize(jsono.get(i).getString("text")));
					if (json.has("cmd"))
						builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
								PlaceholderAPI.setPlaceholders(player, json.getString("cmd"))));
					if (json.has("hover"))
						builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(
								CoreUtils.colorize(PlaceholderAPI.setPlaceholders(player, json.getString("hover"))))));
					if (json.has("https"))
						builder.event(new ClickEvent(ClickEvent.Action.OPEN_URL,
								PlaceholderAPI.setPlaceholders(player, json.getString("https"))));
					if (json.has("console")) {
						UID uid = AnnouncementUtils.createClickID();
						builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
								"console " + uid.toString() + " " + json.getString("console")));
					}
					i = i + 1;
				}
			}
			player.spigot().sendMessage(builder.create());
		}
		if (sound != null)
			player.playSound(player.getLocation(), sound, 10, 0);

		return this;
	}

}
