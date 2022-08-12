package net.lushmc.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.lushmc.core.utils.particles.ParticleFormat;

public class CircleHeadFormat extends ParticleFormat {

	public CircleHeadFormat() {
		changeParticle = true;
		setOption("spots", 20);
		allowedParticles.add(Particle.FALLING_LAVA);
		allowedParticles.add(Particle.FALLING_WATER);
		allowedParticles.add(Particle.TOTEM);
		allowedParticles.add(Particle.CRIT);
		allowedParticles.add(Particle.CRIT_MAGIC);
		allowedParticles.add(Particle.SMOKE_NORMAL);
		allowedParticles.add(Particle.VILLAGER_ANGRY);
		allowedParticles.add(Particle.NOTE);
		allowedParticles.add(Particle.ENCHANTMENT_TABLE);
		allowedParticles.add(Particle.FLAME);
		allowedParticles.add(Particle.HEART);
		allowedParticles.add(Particle.COMPOSTER);
		allowedParticles.add(Particle.DAMAGE_INDICATOR);
		allowedParticles.add(Particle.REDSTONE);

		guiItem = new ItemStack(Material.DIAMOND_HELMET);
		name = "&aCircle Head";

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
		spawnParticle(particle, loc.clone().add(
				Math.cos(Math.toRadians(((i) * (360 / getOptions().getInt("spots"))) * (getOptions().getDouble("r")))),
				2, Math.sin(Math.toRadians(i) * (360 / getOptions().getInt("spots"))) * (getOptions().getDouble("r"))));
		spawnParticle(particle, loc.clone().add(
				Math.cos(
						Math.toRadians(((i + (getOptions().getInt("spots") / 2)) * (360 / getOptions().getInt("spots")))
								* (getOptions().getDouble("r")))),
				2,
				Math.sin(Math.toRadians(i + (getOptions().getInt("spots") / 2)) * (360 / getOptions().getInt("spots")))
						* (getOptions().getDouble("r"))));

	}

}
