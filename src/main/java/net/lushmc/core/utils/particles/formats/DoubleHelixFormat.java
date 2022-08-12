package net.lushmc.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

public class DoubleHelixFormat extends HelixFormat {

	public DoubleHelixFormat() {
		changeParticle = true;
		setOption("cols", 4);
		setOption("spots", 20);

		allowedParticles.clear();

		allowedParticles.add(Particle.COMPOSTER);
		allowedParticles.add(Particle.DOLPHIN);
		allowedParticles.add(Particle.FALLING_WATER);
		allowedParticles.add(Particle.NOTE);
		allowedParticles.add(Particle.END_ROD);
		allowedParticles.add(Particle.REDSTONE);

		name = "&e&lDouble &eHelix";
		guiItem = new ItemStack(Material.REDSTONE_BLOCK);

		particle = Particle.COMPOSTER;
	}

	@Override
	public void display(UUID uid) {
		if (Bukkit.getPlayer(uid) != null) {
			display(Bukkit.getPlayer(uid).getLocation());
		}
	}

	@Override
	public void display(Location loc) {

		super.display(loc);
//		spawnParticle(particle, loc.clone().add(
//				Math.cos(Math.toRadians(((i) * (360 / getOptions().getInt("spots"))))) * (getOptions().getDouble("r")),
//				1 + Math.cos(Math.toRadians(((i) * (360 / 180)))) * (getOptions().getDouble("h")),
//				Math.sin(Math.toRadians(i * (360 / getOptions().getInt("spots")))) * (getOptions().getDouble("r"))));
//		spawnParticle(particle,
//				loc.clone()
//						.add(Math.cos(Math.toRadians(((i + (getOptions().getInt("spots") / 2)))))
//								* (360 / getOptions().getInt("spots")) * (getOptions().getDouble("r")),
//								1 + Math.cos(Math.toRadians(((i)))) * (360 / 180) * (getOptions().getDouble("h")),
//								Math.sin(Math.toRadians(i + (getOptions().getInt("spots") / 2)))
//										* (360 / getOptions().getInt("spots")) * (getOptions().getDouble("r"))));

	}

}
