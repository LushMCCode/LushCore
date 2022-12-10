package net.lushmc.core.utils;

import java.lang.reflect.Field;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
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
			byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
			profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
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
