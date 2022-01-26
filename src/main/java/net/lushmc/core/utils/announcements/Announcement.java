package net.lushmc.core.utils.announcements;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

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

	public Announcement setAnnoucement(String... strings) {
		List<String> l = new ArrayList<>();
		for (String s : strings)
			l.add(s);
		return setAnnoucement(l);
	}

	public Announcement setAnnoucement(List<String> annoucement) {
		this.annoucement = annoucement;
		return this;
	}

	@SuppressWarnings("unchecked")
	public Announcement setAnnoucement(Object strings) {
		return strings instanceof List ? setAnnoucement((List<String>) strings) : setAnnoucement(strings);
	}

	public Announcement announce(Player player) {
		for (String line : annoucement) {
			Bukkit.broadcastMessage(line);
			if (line.contains("{") && line.contains("}")) {
				String jsons = "{" + line.split("{")[0].split("}")[line.split("{")[0].split("}").length];
				Bukkit.broadcastMessage(jsons);
			}
		}
		return this;
	}

}
