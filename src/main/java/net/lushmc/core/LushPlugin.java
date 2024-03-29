package net.lushmc.core;

import org.bukkit.plugin.java.JavaPlugin;

import net.lushmc.core.commands.AdminCommands;
import net.lushmc.core.commands.PlayerCommands;
import net.lushmc.core.listeners.PlayerListener;
import net.lushmc.core.utils.CoreUtils;

public class LushPlugin extends JavaPlugin {

	@Override
	public void onEnable() {

		CoreUtils.init(this);

		new AdminCommands(this, "lush", "debug", "update");
		new PlayerCommands(this, "console");
		
		new PlayerListener(this);
	}

}
