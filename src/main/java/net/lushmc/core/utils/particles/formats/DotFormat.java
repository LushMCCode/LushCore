package net.lushmc.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.lushmc.core.utils.particles.ParticleFormat;

public class DotFormat extends ParticleFormat {

	public DotFormat() {
		changeParticle = true;
		allowedParticles.add(Particle.COMPOSTER);
		allowedParticles.add(Particle.FALLING_WATER);
		allowedParticles.add(Particle.CRIT);
		allowedParticles.add(Particle.REDSTONE);
//		allowedParticles.add(Particle.SNEEZE);
		allowedParticles.add(Particle.CRIT_MAGIC);

		name = "&aDot";
		guiItem = new ItemStack(Material.END_CRYSTAL);
	}

	@Override
	public void display(UUID uid) {
		if (Bukkit.getPlayer(uid) != null)
			display(Bukkit.getPlayer(uid).getEyeLocation());
	}

	@Override
	public void display(Location loc) {
		if (particle == null)
			return;
		spawnParticle(particle, loc);
	}

}
