package net.lushmc.core.utils.items;

import org.bukkit.plugin.java.JavaPlugin;

public class Metadata {

	JavaPlugin plugin;
	Object object;

	public Metadata(JavaPlugin plugin, Object object) {
		this.object = object;
		this.plugin = plugin;
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}

	public Object getObject() {
		return object;
	}

}
