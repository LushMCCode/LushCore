package net.lushmc.core.utils.gui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;

import net.lushmc.core.utils.CoreUtils;

public class GuiManager {

	private static Map<UUID, String> invTracker = new HashMap<>();

	public static void openInventory(Player player, Inventory inventory, String title) {
		if (inventory == null)
			return;
		if (invTracker.containsKey(player.getUniqueId())) {
			switchInventory(player, inventory, title);
			return;
		}
		player.openInventory(inventory);
		invTracker.put(player.getUniqueId(), title);
	}

	public static String getOpenInventory(Player player) {
		return invTracker.containsKey(player.getUniqueId()) ? invTracker.get(player.getUniqueId()) : "none";
	}

	public static void switchInventory(Player player, Inventory inventory, String title) {
		if (inventory == null)
			return;

		player.setMetadata("switchinv", new FixedMetadataValue(CoreUtils.getPlugin(), "yup"));
		player.openInventory(CoreUtils.getGuis().get("waiting").getInventory(player));
		invTracker.put(player.getUniqueId(), "waiting");
		Bukkit.getScheduler().runTaskLater(CoreUtils.getPlugin(), new Runnable() {

			@Override
			public void run() {
				player.openInventory(inventory);
				invTracker.put(player.getUniqueId(), title);
				player.removeMetadata("switchinv", CoreUtils.getPlugin());
			}

		}, 5);
	}

	public static void closeInventory(Player player) {
		if (invTracker.containsKey(player.getUniqueId())) {
			if (invTracker.get(player.getUniqueId()) != "none") {
				invTracker.put(player.getUniqueId(), "none");
				player.closeInventory();
			}

		} else {
			try {
			} catch (Exception ex) {
			}
			invTracker.put(player.getUniqueId(), "none");
		}

	}

}
