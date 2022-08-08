package net.lushmc.core.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitTask;
import org.json2.JSONArray;
import org.json2.JSONObject;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.clip.placeholderapi.PlaceholderAPI;
import net.lushmc.core.LushPlugin;
import net.lushmc.core.utils.announcements.AnnouncementUtils;
import net.lushmc.core.utils.gui.GuiInventory;
import net.lushmc.core.utils.gui.GuiItem;
import net.lushmc.core.utils.gui.GuiManager;
import net.lushmc.core.utils.items.ItemManager;
import net.lushmc.core.utils.levels.LevelUtils;
import net.lushmc.core.utils.placeholders.LushPlaceholderExpansion;
import net.lushmc.core.utils.runnables.Heartbeat;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;

public class CoreUtils {

	public static final String PREFIX = colorize("&3&lGuis&r&f >&7 ");
	public static final String PLUGIN = "MysticGuis";
	private static final int MAX_LIMITED_GUIS = 4;

	static LushPlugin plugin = null;

	private static Map<String, GuiInventory> guis = new HashMap<>();

	private static Economy econ;
	private static Permission perms;
	private static Chat chat;

	private static boolean limited = false;

	private static Map<String, Boolean> deps = new HashMap<>();

	private static File guiFolder = null;

	private static Map<String, String> prefixes = new HashMap<>();
	private static int heartbeat_delay = 6;
	private static BukkitTask heartbeat;

	public static void init(LushPlugin main) {
		plugin = main;
		addPrefix("admin", "&c&lAdmin &8> &7");

		AnnouncementUtils.init();

		LevelUtils.start();
		PlaceholderAPI.registerExpansion(new LushPlaceholderExpansion());

		readConfig();
		guiFolder = new File(plugin.getDataFolder().getPath() + "/guis");

		deps.clear();
		deps.put("vault-econ", setupEconomy());
		deps.put("vault-chat", setupChat());
		deps.put("vault-perm", setupPermissions());
		deps.put("mp", Bukkit.getPluginManager().getPlugin("MysticPlaceholders") != null);

		for (Entry<String, Boolean> e : deps.entrySet())
			log("Dependency check (" + e.getKey() + "): " + e.getValue());

		ItemManager.init();

		registerGuis();

		heartbeat = Bukkit.getScheduler().runTaskLater(getPlugin(), new Heartbeat(heartbeat_delay), 1);
	}

	private static void readConfig() {
		if (plugin.getConfig().isSet("AnnouncementDelay"))
			heartbeat_delay = plugin.getConfig().getInt("AnnouncementDelay");
		else {
			plugin.getConfig().set("AnnouncementDelay", heartbeat_delay);
			plugin.saveConfig();
		}
	}

	public static LushPlugin getPlugin() {
		return plugin;
	}

	public static Map<String, String> prefixes() {
		return prefixes;
	}

	public static String prefixes(String key) {
		if (prefixes.get(key) == null)
			prefixes.put(key, colorize("&e&l" + key.toUpperCase().substring(0, 1)
					+ key.toLowerCase().substring(1, key.length()) + " &7>&f "));
		return prefixes.get(key);
	}

	public static void addPrefix(String key, String value) {
		prefixes.put(key, (colorize(value)));
	}

	public static void log(String log) {
		log(Level.CONFIG, log);
	}

	public static void log(Level level, String log) {
		plugin.getLogger().log(level, colorize(log));
	}

	public static String colorize(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static boolean downloadFile(String url, String filename, String... auth) {

		boolean success = true;
		InputStream in = null;
		FileOutputStream out = null;

		try {

			URL myUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
			conn.setDoOutput(true);
			conn.setReadTimeout(30000);
			conn.setConnectTimeout(30000);
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestMethod("GET");

//			if (auth != null && auth.length >= 2) {
//				String userCredentials = auth[0].trim() + ":" + auth[1].trim();
//				String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
//				conn.setRequestProperty("Authorization", basicAuth);
//			}
			in = conn.getInputStream();
			out = new FileOutputStream(filename);
			int c;
			byte[] b = new byte[1024];
			while ((c = in.read(b)) != -1)
				out.write(b, 0, c);

		}

		catch (Exception ex) {
			log(("There was an error downloading " + filename + ". Check console for details."));
			ex.printStackTrace();
			success = false;
		}

		finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					log(("There was an error downloading " + filename + ". Check console for details."));
					e.printStackTrace();
				}
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					log(("There was an error downloading " + filename + ". Check console for details."));
					e.printStackTrace();
				}
		}
		return success;
	}

	public static void reload() {
		readConfig();
		AnnouncementUtils.reload();
		heartbeat.cancel();
		heartbeat = Bukkit.getScheduler().runTaskLater(getPlugin(), new Heartbeat(heartbeat_delay), 1);
	}

	public static void registerGuis() {

		guis.clear();
		GuiInventory gui = new GuiInventory("waiting", "&7Waiting...", 9, "XXXXXXXXX");
		GuiItem item = new GuiItem("X");
		item.setDisplayName("&7Waiting...");
		item.setMaterial(Material.GRAY_STAINED_GLASS_PANE);
		gui.addItem("X", item);
		guis.put("waiting", gui);
		try {

			if (!guiFolder.exists()) {
				guiFolder.mkdir();
				InputStream in = null;
				FileOutputStream out = null;

				try {

					URL myUrl = new URL("https://downloads.mysticcloud.net/MysticGuis/examples.yml");
					HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
					conn.setDoOutput(true);
					conn.setReadTimeout(30000);
					conn.setConnectTimeout(30000);
					conn.setUseCaches(false);
					conn.setAllowUserInteraction(false);
					conn.setRequestProperty("Content-Type", "application/json");
					conn.setRequestProperty("Accept-Charset", "UTF-8");
					conn.setRequestMethod("GET");
					in = conn.getInputStream();
					out = new FileOutputStream("plugins/" + PLUGIN + "/guis/examples.yml");
					int c;
					byte[] b = new byte[1024];
					while ((c = in.read(b)) != -1)
						out.write(b, 0, c);

				}

				catch (Exception ex) {
					log(("There was an error updating. Check console for details."));
					ex.printStackTrace();
				}

				finally {
					if (in != null)
						try {
							in.close();
						} catch (IOException e) {
							log(("There was an error updating. Check console for details."));
							e.printStackTrace();
						}
					if (out != null)
						try {
							out.close();
						} catch (IOException e) {
							log(("There was an error updating. Check console for details."));
							e.printStackTrace();
						}
				}
			}

			for (File file : guiFolder.listFiles()) {
				if (file.getName().toLowerCase().endsWith(".yml")) {
					loadGuis(file);
				}
			}
		} catch (Exception e) {
			log(("There was an error registering guis."));
			e.printStackTrace();
		}

	}

	public static boolean limited() {
		return limited;
	}

	public static void limit(boolean limit) {
		limited = limit;
	}

	public static boolean dependencyEnabled(String key) {
		key = key.toLowerCase();
		return deps.containsKey(key) ? deps.get(key) : false;
	}

	private static boolean setupEconomy() {
		if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
			log("Vault not installed. Disabling Vault economy functions.");
			return false;
		}

		RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			log(("No existing economy found. Disabling Vault economy functions."));
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	private static boolean setupChat() {
		RegisteredServiceProvider<Chat> rsp = plugin.getServer().getServicesManager().getRegistration(Chat.class);
		try {
			chat = rsp.getProvider();
		} catch (NullPointerException ex) {
			return false;
		}
		return chat != null;
	}

	private static boolean setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager()
				.getRegistration(Permission.class);
		try {
			perms = rsp.getProvider();
		} catch (NullPointerException ex) {
			return false;
		}
		return perms != null;
	}

	public static Permission getPermissions() {
		return perms;
	}

	public static Chat getChat() {
		return chat;
	}

	public static Economy getEconomy() {
		return econ;
	}

	private static void loadGuis(File file) {
		FileConfiguration fc = YamlConfiguration.loadConfiguration(file);

		log("Loading GUIs... (" + file.getName() + ")");
		int x = 0;
		for (String name : fc.getConfigurationSection("guis").getKeys(false)) {
			if (x == MAX_LIMITED_GUIS && limited)
				break;
			log(" - Loading " + name + "...");
			int size = fc.getInt("guis." + name + ".size", 9);
			String sname = colorize(fc.getString("guis." + name + ".name", "Custom GUI"));
			String array = "";
			for (String s : fc.getStringList("guis." + name + ".config")) {
				array = array + s;
			}
			GuiInventory gui = new GuiInventory(name, sname, size, array);
			for (String iid : fc.getConfigurationSection("guis." + name + ".items").getKeys(false)) {
				log("  - Adding item: " + iid);
				GuiItem item = new GuiItem(iid);

				if (!fc.isSet("guis." + name + ".items." + iid + ".custom_item")) {

					if (fc.isSet("guis." + name + ".items." + iid + ".name"))
						item.setDisplayName(fc.getString("guis." + name + ".items." + iid + ".name"));
					if (fc.isSet("guis." + name + ".items." + iid + ".type")) {
						String type = fc.getString("guis." + name + ".items." + iid + ".type");
						if (type.startsWith("PlayerSkull:")) {
							item.setPlayerSkull(type.split(":")[1]);
						} else if (type.startsWith("CustomItem:")) {
							item.setCustomItem(ItemManager.getCustomItem(
									fc.getString("guis." + name + ".items." + iid + ".type").split("ustomItem:")[1]));
						} else
							item.setMaterial(Material
									.valueOf(fc.getString("guis." + name + ".items." + iid + ".type").toUpperCase()));
					}

				} else {
					item.setCustomItem(
							ItemManager.getCustomItem(fc.getString("guis." + name + ".items." + iid + ".custom_item")));
				}

				if (fc.isSet("guis." + name + ".items." + iid + ".lore"))
					item.setLore(fc.getStringList("guis." + name + ".items." + iid + ".lore"));
				if (fc.isSet("guis." + name + ".items." + iid + ".buy"))
					item.setBuyPrice(fc.getString("guis." + name + ".items." + iid + ".buy"));
				if (fc.isSet("guis." + name + ".items." + iid + ".sell"))
					item.setSellPrice(fc.getString("guis." + name + ".items." + iid + ".sell"));

				if (fc.isSet("guis." + name + ".items." + iid + ".action")) {
					item.setSingleAction(true);
					JSONObject json = new JSONObject("{}");
					if (fc.isSet("guis." + name + ".items." + iid + ".action.action"))
						json.put("action", fc.getString("guis." + name + ".items." + iid + ".action.action"));
					if (fc.isSet("guis." + name + ".items." + iid + ".action.server"))
						json.put("server", fc.getString("guis." + name + ".items." + iid + ".action.server"));
					if (fc.isSet("guis." + name + ".items." + iid + ".action.item"))
						json.put("item", fc.getString("guis." + name + ".items." + iid + ".action.item"));
					if (fc.isSet("guis." + name + ".items." + iid + ".action.amount"))
						json.put("amount", fc.getString("guis." + name + ".items." + iid + ".action.amount"));
					if (fc.isSet("guis." + name + ".items." + iid + ".action.message"))
						json.put("message", fc.getString("guis." + name + ".items." + iid + ".action.message"));
					if (fc.isSet("guis." + name + ".items." + iid + ".action.command"))
						json.put("command", fc.getString("guis." + name + ".items." + iid + ".action.command"));
					item.setSingleAction(json);
				}

				if (fc.isSet("guis." + name + ".items." + iid + ".actions")) {
					item.setSingleAction(false);
					JSONArray actions = new JSONArray();

					for (String clickAction : fc.getConfigurationSection("guis." + name + ".items." + iid + ".actions")
							.getKeys(false)) {
						for (String a : fc
								.getConfigurationSection("guis." + name + ".items." + iid + ".actions." + clickAction)
								.getKeys(false)) {
							String key = "guis." + name + ".items." + iid + ".actions." + clickAction;
							JSONObject action = new JSONObject("{}");
							action.put("click", clickAction);
							if (fc.isSet(key + "." + a + ".action"))
								action.put("action", fc.getString(key + "." + a + ".action"));
							if (fc.isSet(key + "." + a + ".server"))
								action.put("server", fc.getString(key + "." + a + ".server"));
							if (fc.isSet(key + "." + a + ".item"))
								action.put("item", fc.getString(key + "." + a + ".item"));
							if (fc.isSet(key + "." + a + ".amount"))
								action.put("amount", fc.getString(key + "." + a + ".amount"));
							if (fc.isSet(key + "." + a + ".message"))
								action.put("message", fc.getString(key + "." + a + ".message"));
							if (fc.isSet(key + "." + a + ".command"))
								action.put("command", fc.getString(key + "." + a + ".command"));
							actions.put(action);
						}
					}
					item.setActions(actions);
				}

				gui.addItem(item.getIdentifier(), item);

			}

			guis.put(name, gui);
			log("Successfully loaded " + name);
			x = x + 1;
		}
	}

	public static List<String> colorizeStringList(List<String> stringList) {
		return colorizeStringList((String[]) stringList.toArray());
	}

	public static List<String> colorizeStringList(String[] stringList) {
		List<String> ret = new ArrayList<>();
		for (String s : stringList) {
			ret.add(colorize(s));
		}
		return ret;
	}

	public static Map<String, GuiInventory> getGuis() {
		return guis;
	}

	public static void sendPluginMessage(Player player, String channel, String... arguments) {
		if (arguments == null | arguments.length == 0)
			return;
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		for (String s : arguments) {
			out.writeUTF(s);
		}
		player.sendPluginMessage(getPlugin(), channel, out.toByteArray());
	}

	public static ItemStack getSkull(String player) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner(player);
		skull.setItemMeta(meta);
		return skull;
	}

	public static boolean update() {

		boolean success = true;
		InputStream in = null;
		FileOutputStream out = null;

		try {

			URL myUrl = new URL(
					"https://jenkins.mysticcloud.net/job/MysticGuis/lastSuccessfulBuild/artifact/target/MysticGuis.jar");
			HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
			conn.setDoOutput(true);
			conn.setReadTimeout(30000);
			conn.setConnectTimeout(30000);
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestMethod("GET");
			in = conn.getInputStream();
			out = new FileOutputStream("plugins/" + PLUGIN + ".jar");
			int c;
			byte[] b = new byte[1024];
			while ((c = in.read(b)) != -1)
				out.write(b, 0, c);

		}

		catch (Exception ex) {
			log(("There was an error updating. Check console for details."));
			ex.printStackTrace();
			success = false;
		}

		finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					log(("There was an error updating. Check console for details."));
					e.printStackTrace();
				}
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					log(("There was an error updating. Check console for details."));
					e.printStackTrace();
				}
		}
		return success;
	}

	public static String setPlaceholders(Player player, String string) {
		string = string.replaceAll("%player%", player.getName());
		if (dependencyEnabled("mp"))
			string = PlaceholderAPI.setPlaceholders(player, string);
		string = colorize(string);
		return string;
	}

	public static boolean processAction(Player player, GuiItem item, JSONObject action) {

		switch (action.getString("action").toLowerCase()) {
		case "sell":
			ItemStack t = item.getItem(player);
			if (action.has("amount"))
				t.setAmount(Integer.parseInt(action.getString("amount")));
			if (player.getInventory().contains(t)) {
				player.getInventory().remove(t);
				getEconomy().depositPlayer(player, item.getSellPrice());
				return true;
			} else
				return false;
		case "send_message":
			player.sendMessage(colorize(action.getString("message")));
			return true;
		case "open_gui":
			try {
				GuiManager.openInventory(player, getGuis().get(action.getString("gui")).getInventory(player),
						action.getString("gui"));
			} catch (NullPointerException ex) {
				player.sendMessage(PREFIX + "There was an error opening that GUI. Does it exist?");
			}
			return true;
		case "join_server":
			sendPluginMessage(player, "BungeeCord", "Connect", action.getString("server"));
			return true;
		case "buy":
			int amount = action.has("amount") ? Integer.parseInt(action.getString("amount")) : 1;
			double price = item.getBuyPrice() * amount;
			if (getEconomy().has(player, price)) {
				getEconomy().withdrawPlayer(player, price);
				if (action.has("item")) {
					ItemStack i = decodeItem(setPlaceholders(player, action.getString("item")));
					i.setAmount(amount);
					player.getInventory().addItem(i);
					return true;
				}
				if (action.has("command")) {
					String sender = action.has("sender") ? action.getString("sender") : "player";
					String cmd = setPlaceholders(player, action.getString("command"));
					Bukkit.dispatchCommand(sender.equalsIgnoreCase("CONSOLE") ? Bukkit.getConsoleSender() : player,
							cmd);
				}
				return true;
			} else
				return false;
		case "command":
			String sender = action.has("sender") ? action.getString("sender") : "player";
			String cmd = setPlaceholders(player, action.getString("command"));
			Bukkit.dispatchCommand(sender.equalsIgnoreCase("CONSOLE") ? Bukkit.getConsoleSender() : player, cmd);
			return true;
		case "close_gui":
			player.closeInventory();
			return true;
		}
		if (action.has("error_message"))
			player.sendMessage(setPlaceholders(player, action.getString("error_message")));
		return false;
	}

	public static ItemStack decodeItem(String itemdata) {
		ItemStack i = new ItemStack(Material.AIR);
		if (itemdata.startsWith("CustomItem:")) {
			// TODO
			return i;
		}
		if (itemdata.startsWith("PlayerSkull:")) {
			i = getSkull(itemdata.split(":")[1]);
			return i;
		}
		i = new ItemStack(Material.valueOf(itemdata.toUpperCase()));
		return i;
	}

}
