package net.lushmc.core.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import net.lushmc.core.LushPlugin;
import net.md_5.bungee.api.ChatColor;

public class CoreUtils {

	private static LushPlugin plugin;
	private static Map<String, String> prefixes = new HashMap<>();

	public static void init(LushPlugin main) {
		plugin = main;
		addPrefix("admin", "&c&lAdmin &8> &7");
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

}
