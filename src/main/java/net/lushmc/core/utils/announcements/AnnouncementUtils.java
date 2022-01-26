package net.lushmc.core.utils.announcements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

import net.lushmc.core.utils.CoreUtils;

public class AnnouncementUtils {

	public static Map<String, Announcement> announcements = new HashMap<>();

	public void init() {
		FileConfiguration conf = CoreUtils.getPlugin().getConfig();
		if (conf.isSet("Announcements")) {
			for (String name : CoreUtils.getPlugin().getConfig().getConfigurationSection("Annoucements")
					.getKeys(false)) {
				String key = "Annoucements." + name;
				Announcement a = new Announcement(name);
				if (conf.isSet(key + ".Enabled"))
					a.setEnabled(conf.getBoolean(key + ".Enabled"));
				if (conf.isSet(key + ".Sound"))
					a.setSound(Sound.valueOf(conf.getString(key + ".Sound")));
				if (conf.isSet(key + ".Annoucement"))
					a.setAnnoucement(
							conf.get(key + ".Annoucement") instanceof List ? conf.getStringList(key + ".Annoucement")
									: conf.getString(key + ".Annoucement"));

				announcements.put(name, a);
			}
		}
	}

	public static Map<String, Announcement> getAnnouncements() {
		return announcements;
	}

}
