package net.lushmc.core.utils.runnables;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.lushmc.core.utils.CoreUtils;
import net.lushmc.core.utils.announcements.Announcement;
import net.lushmc.core.utils.announcements.AnnouncementUtils;

public class Heartbeat implements Runnable {

	long lastAnnouncement = 0;

	@Override
	public void run() {

		if (new Date().getTime() - lastAnnouncement >= TimeUnit.MILLISECONDS.convert(9, TimeUnit.HOURS)) {
			lastAnnouncement = new Date().getTime();
			Announcement a = AnnouncementUtils.getAnnouncements().get("test");
			for (Player player : Bukkit.getOnlinePlayers()) {
				a.announce(player);
			}
		}

		if (CoreUtils.getPlugin().isEnabled())
			Bukkit.getScheduler().runTaskLater(CoreUtils.getPlugin(), this, 1);
	}

}
