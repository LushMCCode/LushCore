package net.lushmc.core.utils;

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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitTask;
import org.json2.JSONObject;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.clip.placeholderapi.PlaceholderAPI;
import net.lushmc.core.LushPlugin;
import net.lushmc.core.utils.announcements.AnnouncementUtils;
import net.lushmc.core.utils.items.ItemManager;
import net.lushmc.core.utils.levels.LevelUtils;
import net.lushmc.core.utils.placeholders.Emoticons;
import net.lushmc.core.utils.placeholders.LushPlaceholderExpansion;
import net.lushmc.core.utils.runnables.Heartbeat;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;

public class CoreUtils {

	public static final String PREFIX = colorize("&3&lGuis&r&f >&7 ");
	public static final String PLUGIN = "MysticGuis";

	static LushPlugin plugin = null;

	private static Economy econ;
	private static Permission perms;
	private static Chat chat;

	private static Map<String, Boolean> deps = new HashMap<>();

	private static Map<String, String> prefixes = new HashMap<>();
	private static int heartbeat_delay = 6;
	private static BukkitTask heartbeat;

	// subject to change
	private static Map<String, Radio> radios = new HashMap<>();

	@SuppressWarnings("deprecation")
	public static void init(LushPlugin main) {
		main.getLogger().log(Level.INFO, "Setting plugin");
		plugin = main;
		log("Initializing managers...");
		addPrefix("admin", "&c&lAdmin &8" + Emoticons.RIGHT_ARROW + " &7");
		AnnouncementUtils.init();
		LevelUtils.start();
		ItemManager.init();
		log("Register placeholders");
		PlaceholderAPI.registerExpansion(new LushPlaceholderExpansion());
		log("Reading config...");
		readConfig();
		log("Registering dependencies...");
		deps.clear();
		deps.put("vault-econ", setupEconomy());
		deps.put("vault-chat", setupChat());
		deps.put("vault-perm", setupPermissions());
		deps.put("mp", Bukkit.getPluginManager().getPlugin("MysticPlaceholders") != null);

		for (Entry<String, Boolean> e : deps.entrySet())
			log("Dependency check (" + e.getKey() + "): " + e.getValue());

		log("Start heartbeat");
		heartbeat = Bukkit.getScheduler().runTaskLater(getPlugin(), new Heartbeat(heartbeat_delay), 1);
		log("Tune radio");
		addRadio(new CoreRadio(plugin));
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
			prefixes.put(key, colorize("&c&l" + key.toUpperCase().substring(0, 1)
					+ key.toLowerCase().substring(1, key.length()) + " &7" + Emoticons.RIGHT_ARROW + "&f "));
		return prefixes.get(key);
	}

	public static void addPrefix(String key, String value) {
		prefixes.put(key, (colorize(value)));
	}

	public static void log(String log) {
		log(Level.INFO, log);
	}

	public static void log(Level level, String log) {
		plugin.getLogger().log(level, colorize(log));
	}

	public static String colorize(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static boolean updatePlugin(String name, String status) {
		if (status.equalsIgnoreCase("official"))
			status = "";
		else
			status = "_" + status;

		String filename = name + status + ".jar";
		String url = "https://downloads.quickscythe.com/plugins/" + filename;

		return CoreUtils.downloadFile(url, "plugins/" + name + ".jar", "QuickScythe", "r6Pt#BF#Lg73@s4t");
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

			if (auth != null && auth.length >= 2) {
				String userCredentials = auth[0].trim() + ":" + auth[1].trim();
				String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
				conn.setRequestProperty("Authorization", basicAuth);
			}
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

	public static void sendPluginMessage(Player player, String channel, String... arguments) {
		if (arguments == null | arguments.length == 0)
			return;
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		for (String s : arguments) {
			out.writeUTF(s);
		}
		player.sendPluginMessage(getPlugin(), channel, out.toByteArray());
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getSkull(String player) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner(player);
		skull.setItemMeta(meta);
		return skull;
	}

	public static String setPlaceholders(Player player, String string) {
		string = string.replaceAll("%player%", player.getName());
		if (dependencyEnabled("mp"))
			string = PlaceholderAPI.setPlaceholders(player, string);
		string = colorize(string);
		return string;
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

	@SuppressWarnings("deprecation")
	public static String encryptItemStack(ItemStack i) {
		try {
			return i.getType() + ":" + i.getAmount() + ":" + i.getDurability();
		} catch (NullPointerException ex) {
			return "AIR:1:0";
		}

	}

	@Deprecated
	public static ItemStack decryptItemStack(String s) {
		String[] d = s.split(":");
		ItemStack i = s
				.contains(":")
						? (d.length >= 2
								? (d.length == 2 ? new ItemStack(Material.valueOf(d[0]), 1, Short.parseShort(d[1]))
										: new ItemStack(Material.valueOf(d[0]), Integer.parseInt(d[1]),
												Short.parseShort(d[2])))
								: new ItemStack(Material.valueOf(d[0])))
						: new ItemStack(Material.valueOf(s));
		ItemMeta m = i.getItemMeta();
		String j = "";
		if (d.length >= 3)
			for (int f = 3; f != d.length; f++) {
				j = j == "" ? d[f] : j + ":" + d[f];
			}
		JSONObject json = new JSONObject("{}");
		if (j.contains("{") && j.contains("}")) {
			json = new JSONObject(j);
		}

		if (!json.isEmpty()) {
			if (json.has("PotionMeta")) {
				PotionMeta meta = (PotionMeta) m;
				meta.setBasePotionData(new PotionData(PotionType.valueOf(json.getString("PotionMeta"))));
			}
		}
		i.setItemMeta(m);
		return i;

	}

	public static String encryptLocation(Location loc) {
		String r = loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":"
				+ loc.getPitch() + ":" + loc.getYaw();
		r = r.replaceAll("\\.", ",");
		r = "location:" + r;
		return r;
	}

	public static Location decryptLocation(String s) {
		if (s.startsWith("location:"))
			s = s.replaceAll("location:", "");

		if (s.contains(","))
			s = s.replaceAll(",", ".");
		String[] args = s.split(":");
		Location r = new Location(Bukkit.getWorld(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]),
				Double.parseDouble(args[3]));
		if (args.length >= 5) {
			r.setPitch(Float.parseFloat(args[4]));
			r.setYaw(Float.parseFloat(args[5]));
		}
		return r;
	}

	public static void addRadio(Radio radio) {
		radios.put(radio.getName(), radio);
	}

	public static Radio getRadio(String name) {
		return radios.containsKey(name) ? radios.get(name) : null;
	}

	public static Map<String, Radio> getRadios() {
		return radios;
	}

}
