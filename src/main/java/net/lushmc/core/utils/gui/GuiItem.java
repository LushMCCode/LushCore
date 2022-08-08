package net.lushmc.core.utils.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json2.JSONArray;
import org.json2.JSONObject;

import net.lushmc.core.utils.CoreUtils;
import net.lushmc.core.utils.PlayerSkull;
import net.lushmc.core.utils.items.CustomItem;

public class GuiItem {
	String id;
	String display_name = "default_name";
	Material mat = Material.GRASS_BLOCK;
	List<String> lore = null;
	boolean single_action = false;
	double buy = 0;
	double sell = 0;
	JSONObject action = new JSONObject();
	JSONArray actions = new JSONArray();
	boolean does_action = false;
	ItemStack storedItem = null;
	boolean playerSkull = false;
	PlayerSkull skull = null;
	CustomItem citem = null;

	public GuiItem(String id) {
		this.id = id;
	}

	public void setCustomItem(CustomItem citem) {
		this.citem = citem;
	}

	public void setDisplayName(String display_name) {
		this.display_name = display_name;
	}

	public void setMaterial(Material mat) {
		this.mat = mat;
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	public void setSingleAction(boolean single_action) {
		this.single_action = single_action;
	}

	public boolean isSingleAction() {
		return single_action;
	}

	public void setBuyPrice(String string) {
		try {
			buy = Double.parseDouble(string);
		} catch (NumberFormatException ex) {
		}
	}

	public double getBuyPrice() {
		return buy;
	}

	public void setSellPrice(String string) {
		try {
			sell = Double.parseDouble(string);
		} catch (NumberFormatException ex) {
		}
	}

	public double getSellPrice() {
		return sell;
	}

	public void setSingleAction(JSONObject json) {
		does_action = true;
		this.action = json;
	}

	public void setActions(JSONArray actions) {
		does_action = true;
		this.actions = actions;
	}

	public JSONObject getAction() {
		return action;
	}

	public JSONArray getActions() {
		return actions;
	}

	public String getIdentifier() {
		return id;
	}

	public ItemStack getItem(Player player) {
		if (storedItem == null) {
			ItemStack item = playerSkull ? skull.getSkull(player)
					: (citem == null ? new ItemStack(mat) : citem.getItem(player));
			ItemMeta meta = item.getItemMeta();
			if (lore != null) {
				List<String> tmp = new ArrayList<>();
				if (meta.hasLore())
					for (String a : meta.getLore())
						tmp.add(a);
				for (String a : lore) {
					tmp.add(CoreUtils.setPlaceholders(player, a));
				}
				meta.setLore(tmp);
			}
			meta.addItemFlags(ItemFlag.values());
			if (citem == null)
				meta.setDisplayName(CoreUtils.setPlaceholders(player, display_name));
			item.setItemMeta(meta);
			this.storedItem = item;
		}
		return this.storedItem.clone();
	}

	public boolean hasAction() {
		return does_action;
	}

	public void setPlayerSkull(String string) {
		playerSkull = true;
		skull = new PlayerSkull(string);
	}

}
