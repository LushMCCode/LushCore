package net.lushmc.core.utils.items;

import java.lang.reflect.Field;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class PlayerSkull {

	OfflinePlayer player;
	String url = "";

	public PlayerSkull(String url) {
		this.url = url;
	}

	public PlayerSkull(OfflinePlayer player) {
		this.player = player;
	}

	public ItemStack getSkull() {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		if (url.equals(""))
			meta.setOwningPlayer(player);
		else {
			GameProfile profile = new GameProfile(UUID.randomUUID(), null);
			profile.getProperties().put("textures", new Property("textures", url));
			Field profileField = null;

			try {
				profileField = meta.getClass().getDeclaredField("profile");
			} catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}

			profileField.setAccessible(true);

			try {
				profileField.set(meta, profile);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

		}
		skull.setItemMeta(meta);
		return skull;
	}

}
