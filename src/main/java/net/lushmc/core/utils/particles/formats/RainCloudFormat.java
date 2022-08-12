package net.lushmc.core.utils.particles.formats;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.lushmc.core.utils.particles.ParticleFormat;

public class RainCloudFormat extends ParticleFormat {

	private Location cloc = null;
	double scalar = 6;

	public RainCloudFormat() {

		setOption("spots", 30);
		name = "&7Rain Cloud";
		guiItem = new ItemStack(Material.GRAY_DYE);
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
		if (0.05 > new Random().nextDouble() * 100) {
			for (int z = 0; z != 5 * scalar; i++) {
				loc.add((new Random().nextDouble()
						* (new Random().nextBoolean() ? (double) 1 / scalar : -(double) 1 / scalar)), -1 / scalar,
						(new Random().nextDouble()
								* (new Random().nextBoolean() ? (double) 1 / scalar : -(double) 1 / scalar)));
				spawnParticle(Particle.END_ROD, loc);
			}
		}
		if (i % 2 == 0)
			for (int a = 0; a != 11; a++) {
				for (int t = 0; t != getOptions().getInt("spots") + 1; t++) {
					spawnParticle(Particle.CLOUD,
							cloc.clone()
									.add(Math.cos(t * (360 / getOptions().getInt("spots")))
											* (a * (getOptions().getDouble("r") / 10)), getOptions().getDouble("h") + 1,
											Math.sin(t * (360 / getOptions().getInt("spots")))
													* (a * (getOptions().getDouble("r") / 10))));
				}

			}
	}

}
