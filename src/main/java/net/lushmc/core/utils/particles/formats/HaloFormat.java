package net.lushmc.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.lushmc.core.utils.particles.ParticleFormat;
import net.lushmc.core.utils.particles.ParticleUtils;

public class HaloFormat extends ParticleFormat {

	public HaloFormat() {
		changeParticle = true;
		allowedParticles.add(Particle.NAUTILUS);
		allowedParticles.add(Particle.WATER_DROP);
		allowedParticles.add(Particle.FALLING_LAVA);
		allowedParticles.add(Particle.FALLING_WATER);

		name = "&cHalo";
		guiItem = new ItemStack(Material.GOLDEN_HELMET);
		allowedParticles.add(Particle.REDSTONE);
		setOption("r", 0.5);
		setOption("h", 2.25);
		setOption("spots", getOptions().optDouble("spots", 40) / 2);
		setOption("size", 0.4f);
	}

	@Override
	public void display(UUID uid) {
		if (Bukkit.getPlayer(uid) != null) {
			display(Bukkit.getPlayer(uid).getEyeLocation());
		}
	}

	@Override
	public void display(Location loc) {
		if (particle == null)
			return;
		for (int a = 1; a != getOptions().getInt("spots") + 1; a++) {
			spawnParticle(particle,
					loc.clone()
							.add(ParticleUtils.rotateAroundAxisY(new Vector(
									Math.cos(Math.toRadians(a) * ((360) / (getOptions().getInt("spots"))))
											* (getOptions().getDouble("r")),
									getOptions().getDouble("h"),
									Math.sin(Math.toRadians(a) * ((360) / (getOptions().getInt("spots"))))
											* (getOptions().getDouble("r"))),
									i)));

		}

	}

}
