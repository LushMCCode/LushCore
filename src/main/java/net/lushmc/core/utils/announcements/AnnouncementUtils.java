package net.lushmc.core.utils.announcements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.lushmc.core.utils.CoreUtils;
import net.lushmc.core.utils.UID;

public class AnnouncementUtils {

	private static Map<String, Announcement> announcements = new HashMap<>();

	private static List<UID> clickIds = new ArrayList<>();
	private static File announcefile = null;

	public static void init() {
		announcefile = new File(CoreUtils.getPlugin().getDataFolder().getPath() + "/announcements.yml");

		if (!announcefile.exists()) {
			try {
				announcefile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		FileConfiguration conf = YamlConfiguration.loadConfiguration(announcefile);
		if (conf.isSet("Announcements")) {
			for (String name : conf.getConfigurationSection("Announcements").getKeys(false)) {
				String key = "Announcements." + name;
				Announcement a = new Announcement(name);
				if (conf.isSet(key + ".Enabled"))
					a.setEnabled(conf.getBoolean(key + ".Enabled"));
				if (conf.isSet(key + ".Sound"))
					a.setSound(Sound.valueOf(conf.getString(key + ".Sound")));
				if (conf.isSet(key + ".Announcement"))
					a.setAnnouncement(
							conf.get(key + ".Announcement") instanceof List ? conf.getStringList(key + ".Announcement")
									: conf.getString(key + ".Announcement"));

				announcements.put(name, a);
			}
		} else {
			conf.set("Announcements.Test1.Enabled", true);
			conf.set("Announcements.Test1.Sound", Sound.BLOCK_NOTE_BLOCK_BIT.name());
			conf.set("Announcements.Test1.Announcement",
					"&fType {\"text\":\"&a/spawn\",\"hover\":\"&aClick to teleport\",\"cmd\":\"spawn\"} &fto teleport to spawn.");

			conf.set("Announcements.Test2.Enabled", true);
			List<String> list = new ArrayList<>();
			list.add("&a--------------------------");
			list.add("&r");
			list.add(
					"&fDon't forget to check out our {\"text\":\"&bforums\",\"hover\":\"&aClick to open\",\"https\":\"https://www.mysticcloud.net\"}&f!");
			list.add("&r");
			list.add("&a--------------------------");
			conf.set("Announcements.Test2.Announcement", list);

			try {
				conf.save(announcefile);
			} catch (IOException e) {
				e.printStackTrace();
			}

			for (String name : conf.getConfigurationSection("Announcements").getKeys(false)) {
				String key = "Announcements." + name;
				Announcement a = new Announcement(name);
				if (conf.isSet(key + ".Enabled"))
					a.setEnabled(conf.getBoolean(key + ".Enabled"));
				if (conf.isSet(key + ".Sound"))
					a.setSound(Sound.valueOf(conf.getString(key + ".Sound")));
				if (conf.isSet(key + ".Announcement"))
					a.setAnnouncement(
							conf.get(key + ".Announcement") instanceof List ? conf.getStringList(key + ".Announcement")
									: conf.getString(key + ".Announcement"));

				announcements.put(name, a);
			}

		}
	}

	public static Map<String, Announcement> getAnnouncements() {
		return announcements;
	}

	public static UID createClickID() {
		UID cid = new UID(5);
		while (clickIds.contains(cid))
			cid = new UID(5);
		clickIds.add(cid);
		return cid;
	}

	public static boolean hasClickId(UID cid) {
		return clickIds.contains(cid);
	}

}
