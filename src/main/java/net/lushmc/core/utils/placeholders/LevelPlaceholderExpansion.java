package net.lushmc.core.utils.placeholders;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.lushmc.core.utils.levels.LevelUtils;

public class LevelPlaceholderExpansion extends PlaceholderExpansion {

	@Override
	public String getAuthor() {
		return "QuickScythe";
	}

	@Override
	public String getIdentifier() {
		return "levelpapi";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
		if (player == null)
			return null;
		if (params.equalsIgnoreCase("level")) {
			return "" + (int) LevelUtils.getMainWorker().getLevel((long) (player.getPlayer().getExp() * 100));
		}
		return null;
	}
}
