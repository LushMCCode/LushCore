package net.lushmc.core.utils.radio;


import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Radio {

	String name;
	JavaPlugin plugin;

	public Radio(String name, JavaPlugin plugin) {
		this.name = name;
		this.plugin = plugin;
	}

	public void runTask(Runnable run, int delay) {
		plugin.getServer().getScheduler().runTaskLater(plugin, run, delay);
	}

	public String getName() {
		return name;
	}

}
