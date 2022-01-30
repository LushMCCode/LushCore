package net.lushmc.core.utils.runnables;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.lushmc.core.utils.CoreUtils;
import net.lushmc.core.utils.announcements.Announcement;
import net.lushmc.core.utils.announcements.AnnouncementUtils;

public class Heartbeat implements Runnable {

	long lastAnnouncement = 0;
	int delay;

	public Heartbeat(int delay) {
		this.delay = delay;
	}

	@Override
	public void run() {

		if (new Date().getTime() - lastAnnouncement >= TimeUnit.MILLISECONDS.convert(delay, TimeUnit.MINUTES)) {
			lastAnnouncement = new Date().getTime();
			Announcement a = (Announcement) AnnouncementUtils.getAnnouncements().values().toArray()[new Random()
					.nextInt(AnnouncementUtils.getAnnouncements().size())];
			while (!a.isEnabled())
				a = (Announcement) AnnouncementUtils.getAnnouncements().values().toArray()[new Random()
						.nextInt(AnnouncementUtils.getAnnouncements().size())];
			for (Player player : Bukkit.getOnlinePlayers()) {
				a.announce(player);
			}
		}

		if (CoreUtils.getPlugin().isEnabled())
			Bukkit.getScheduler().runTaskLater(CoreUtils.getPlugin(), this, 1);
	}

}
