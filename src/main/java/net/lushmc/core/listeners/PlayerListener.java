package net.lushmc.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.lushmc.core.LushPlugin;
import net.lushmc.core.utils.players.PlayerManager;

public class PlayerListener implements Listener {

	public PlayerListener(LushPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		PlayerManager.getPlayer(e.getPlayer().getUniqueId()).save();
	}

}
