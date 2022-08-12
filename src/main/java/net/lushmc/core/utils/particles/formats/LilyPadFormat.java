package net.lushmc.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.lushmc.core.utils.particles.ParticleFormat;

public class LilyPadFormat extends ParticleFormat {

	Location cloc = null;
	int rspots = 130;
	int corners = 4;

	public LilyPadFormat() {
		name = "&2Lily Pad";
		guiItem = new ItemStack(Material.LILY_PAD);
		particle = Particle.COMPOSTER;
	}

	@Override
	public void display(UUID uid) {
		if (Bukkit.getPlayer(uid) != null)
			display(Bukkit.getPlayer(uid).getLocation());
	}

	@Override
	public void display(Location loc) {
		if (particle == null)
			return;
		cloc = loc.clone();

		for (int t = 0; t != corners; t++) {
			spawnParticle(particle,
					cloc.clone().add(
							Math.cos(Math.toRadians(i + ((getOptions().getInt("spots") / (corners)) * t))
									* (360 / getOptions().getInt("spots")))
									* (Math.cos(Math.toRadians(i) * (360 / rspots) * (getOptions().getDouble("r")))),
							0.05,
							Math.sin(Math.toRadians(i + ((getOptions().getInt("spots") / (corners)) * t))
									* (360 / getOptions().getInt("spots")))
									* (Math.cos(Math.toRadians(i) * (360 / rspots) * (getOptions().getDouble("r"))))));
		}

		for (int t = 0; t != corners; t++) {
			spawnParticle(particle, cloc.clone().add(Math
					.cos(Math.toRadians(i + ((getOptions().getInt("spots") / (corners)) * t))
							* (360 / getOptions().getInt("spots")))
					* (Math.cos(Math.toRadians((i + (rspots / 4))) * (360 / rspots) * (getOptions().getDouble("r")))),
					0.05,
					Math.sin(Math.toRadians(i + ((getOptions().getInt("spots") / (corners)) * t))
							* (360 / getOptions().getInt("spots")))
							* (Math.cos(Math.toRadians((i + (rspots / 4))) * (360 / rspots)
									* (getOptions().getDouble("r"))))));
		}

	}

}
