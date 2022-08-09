package net.lushmc.core.utils.placeholders;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface PlaceholderWorker {

	public abstract String run(Player player);

}
