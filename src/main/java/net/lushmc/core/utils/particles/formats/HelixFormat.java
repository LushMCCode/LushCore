package net.lushmc.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.lushmc.core.utils.particles.ParticleFormat;

public class HelixFormat extends ParticleFormat {
	Location cloc = null;
	int colspots = 180;

	public HelixFormat() {
		changeParticle = true;

		allowedParticles.add(Particle.COMPOSTER);
		allowedParticles.add(Particle.DOLPHIN);
		allowedParticles.add(Particle.FALLING_WATER);
		allowedParticles.add(Particle.NOTE);
		allowedParticles.add(Particle.END_ROD);
		allowedParticles.add(Particle.REDSTONE);

		name = "&eHelix";
		guiItem = new ItemStack(Material.REDSTONE);
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
		for (int t = 0; t != getOptions().getInt("cols"); t++) {
			cloc = loc.clone().add(
					Math.cos(Math.toRadians(((i + ((getOptions().getInt("spots") / getOptions().getInt("cols")) * t))
							* (360 / getOptions().getInt("spots"))))) * (getOptions().getDouble("r")),
					Math.cos(Math.toRadians(((i) * (360 / colspots)))) * (getOptions().getDouble("h")),
					Math.sin(Math.toRadians(i + ((getOptions().getInt("spots") / getOptions().getInt("cols")) * t))
							* (360 / getOptions().getInt("spots"))) * (getOptions().getDouble("r")));
//			cloc = loc.clone().add(
//					Math.cos(Math.toRadians(i + ((getOptions().getInt("spots") / getOptions().getInt("cols")) * t))
//							* (360 / getOptions().getInt("spots")) * (getOptions().getDouble("r"))),
//					1 + Math.cos(Math.toRadians(((i) * (360 / colspots)) * (getOptions().getDouble("r") * 2))),
//					Math.sin(Math.toRadians(i + ((getOptions().getInt("spots") / getOptions().getInt("cols")) * t))
//							* (360 / getOptions().getInt("spots"))) * (getOptions().getDouble("r")));
			spawnParticle(particle, cloc);
		}
//		spawnParticle(uid, particle,
//				Bukkit.getPlayer(uid).getLocation().clone().add(Math.cos(Math.toRadians(((i) * (360 / 20)) * (1))),
//						1 + Math.cos(Math.toRadians(((i) * (360 / 180)) * (2))),
//						Math.sin(Math.toRadians(i) * (360 / 20)) * (1)));
//		spawnParticle(uid, particle,
//				Bukkit.getPlayer(uid).getLocation().clone().add(
//						Math.cos(Math.toRadians(((i + 10) * (360 / 20)) * (1))),
//						1 + Math.cos(Math.toRadians(((i) * (360 / 180)) * (2))),
//						Math.sin(Math.toRadians(i + 10) * (360 / 20)) * (1)));

	}

}
