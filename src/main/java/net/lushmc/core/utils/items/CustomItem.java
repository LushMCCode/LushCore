package net.lushmc.core.utils.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.lushmc.core.utils.CoreUtils;
import net.lushmc.core.utils.PlayerSkull;

public class CustomItem {
	private Material type;
	private int amount = 1;
	private String dname = "default_name";
	private boolean unbreakable = false;
	private List<EnchantmentWrapper> enchantments = new ArrayList<>();
	private List<String> lore = new ArrayList<>();
	private List<ItemFlag> itemFlags = new ArrayList<>();
	private PlayerSkull skull = null;

	private CustomItem(CustomItem clone) {
		this.type = clone.type;
		this.amount = clone.amount;
		this.dname = clone.dname;
		this.unbreakable = clone.unbreakable;
		this.enchantments = clone.enchantments;
		this.lore = clone.lore;
		this.itemFlags = clone.itemFlags;
	}

	public CustomItem(Material type) {
		this.type = type;
	}

	public CustomItem(PlayerSkull skull) {
		this.skull = skull;
	}

	public void setType(Material type) {
		this.type = type;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public CustomItem clone() {
		return new CustomItem(this);
	}

	public Material getType() {
		return type;
	}

	public void setDisplayName(String dname) {
		this.dname = CoreUtils.colorize(dname);
	}

	public void setUnbreakable(boolean unbreakable) {
		this.unbreakable = unbreakable;
	}

	public void addEnchant(Enchantment en, int strength, boolean ambient) {
		enchantments.add(new EnchantmentWrapper(en, strength, ambient));
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	public void addItemFlags(ItemFlag... flags) {
		for (ItemFlag flag : flags)
			itemFlags.add(flag);
	}

	public int getAmount() {
		return amount;
	}

	public boolean hasDisplayName() {
		return !dname.equals("default_name");
	}

	public String getDisplayName() {
		return dname;
	}

	public ItemStack getItem(Player player) {
		List<String> lore = new ArrayList<>();
		for (String s : this.lore)
			lore.add(CoreUtils.setPlaceholders(player, s));
		ItemStack item = skull == null ? new ItemStack(type) : skull.getSkull();
		item.setAmount(amount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(CoreUtils.setPlaceholders(player, dname));
		meta.setUnbreakable(unbreakable);
		for (EnchantmentWrapper enw : enchantments) {
			meta.addEnchant(enw.getEnchantment(), enw.getStrength(), enw.getAmbient());
		}
		if (!lore.isEmpty())
			meta.setLore(lore);
		if (!itemFlags.isEmpty())
			for (ItemFlag flag : itemFlags)
				meta.addItemFlags(flag);

		item.setItemMeta(meta);
		return item;
	}

}
