package net.lushmc.core.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class CosmeticUtils {

	static Map<String, List<UUID>> genericCooldowns = new HashMap<>();

	public static void startGenericCooldown(Player p, String name, Runnable start, int cooldown, BossBar bar,
			Runnable finish) {
		if (!getGenericCooldown(name).contains(p.getUniqueId())) {
			getGenericCooldown(name).add(p.getUniqueId());
			start.run();
			Bukkit.getScheduler().runTaskLater(CoreUtils.getPlugin(),
					new GenericCooldownRunnable(bar, name, p.getUniqueId(), new Date().getTime(), cooldown, finish), 1);

		}

	}

	public static void removeGenericCooldown(UUID uid, String name) {
		if (!genericCooldowns.containsKey(name)) {
			genericCooldowns.put(name, new ArrayList<>());
			return;
		}
		if (genericCooldowns.get(name).contains(uid))
			genericCooldowns.get(name).remove(uid);
	}

	public static Map<String, List<UUID>> getGenericCooldowns() {
		return genericCooldowns;
	}

	public static List<UUID> getGenericCooldown(String name) {
		if (!genericCooldowns.containsKey(name))
			genericCooldowns.put(name, new ArrayList<UUID>());

		return genericCooldowns.get(name);
	}

	public static void useGenericCooldown(String name, String display, Player player, BarColor color, int delay) {
		useGenericCooldown(name, display, player, color, delay, new Runnable() {

			@Override
			public void run() {
			}
		});
	}

	public static void useGenericCooldown(String name, String display, Player player, BarColor color, int delay,
			Runnable finish) {
		if (!genericCooldowns.containsKey(name))
			genericCooldowns.put(name, new ArrayList<>());

		if (!genericCooldowns.get(name).contains(player.getUniqueId())) {
			genericCooldowns.get(name).add(player.getUniqueId());
			BossBar bar = Bukkit.createBossBar(CoreUtils.colorize(display), color, BarStyle.SOLID);
			bar.addPlayer(player);
			Bukkit.getScheduler().runTaskLater(CoreUtils.getPlugin(), new GenericCooldownRunnable(bar, name,
					player.getUniqueId(), new Date().getTime(), delay * 5, finish), 1);
		}
	}

	public static class GenericCooldownRunnable implements Runnable {

		BossBar bar;
		UUID uid;
		long started;
		long cooldown;
		String name;
		Runnable finish;
		boolean perm;

		public GenericCooldownRunnable(BossBar bar, String name, UUID uid, long started, long cooldown,
				Runnable finish) {
			this(bar, name, uid, started, cooldown, finish, true);
		}

		public GenericCooldownRunnable(BossBar bar, String name, UUID uid, long started, long cooldown, Runnable finish,
				boolean perm) {
			this.uid = uid;
			this.bar = bar;
			this.started = started;
			this.name = name;
			this.cooldown = cooldown;
			this.finish = finish;
			this.perm = perm;
			if (Bukkit.getPlayer(uid) != null) {
				this.bar.addPlayer(Bukkit.getPlayer(uid));
			}
		}

		@Override
		public void run() {
			double percent = /*
								 * perm ? (Bukkit.getPlayer(uid).hasPermission("mysticcloud.hub." + name +
								 * ".override") ? ((new Date().getTime() - started) / (0.4)) / 10 : ((new
								 * Date().getTime() - started) / (cooldown)) / 10) :
								 */ ((new Date().getTime() - started) / (cooldown)) / 10;
			if (Bukkit.getPlayer(uid) == null || percent >= 100 || cooldown == -1) {
				removeGenericCooldown(uid, name);
				bar.setProgress(0);
				bar.removeAll();
				Bukkit.getScheduler().runTaskLater(CoreUtils.getPlugin(), finish, 1);
				return;
			}
			bar.setProgress((float) ((100 - percent) / 100));

			Bukkit.getScheduler().runTaskLater(CoreUtils.getPlugin(),
					new GenericCooldownRunnable(bar, name, uid, started, cooldown, finish), 1);

		}

	}

}
