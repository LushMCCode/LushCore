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

public class AtomicFormat extends ParticleFormat {

	public AtomicFormat() {
		setOption("spots", 40);
		changeParticle = true;
		allowedParticles.add(Particle.SPELL_INSTANT);
		allowedParticles.add(Particle.SPELL_MOB);
		allowedParticles.add(Particle.SPELL_WITCH);
		allowedParticles.add(Particle.PORTAL);
		allowedParticles.add(Particle.COMPOSTER);
		allowedParticles.add(Particle.FLAME);
		allowedParticles.add(Particle.REDSTONE);

		guiItem = new ItemStack(Material.HEART_OF_THE_SEA);
		name = "&cAtomic";

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

		spawnParticle(particle,
				loc.clone().add(ParticleUtils.rotateAroundAxisY(
						new Vector(
								Math.cos(Math.toRadians((i + ((getOptions().getInt("spots") / 6) * 1))
										* (360 / getOptions().getInt("spots")) * (getOptions().getDouble("r")))),
								1 + ((Math
										.cos(Math.toRadians((i + ((getOptions().getInt("spots") / 6) * 1))
												* (360 / getOptions().getInt("spots")) * (getOptions().getDouble("r"))))
										* -1)),
								Math.sin(Math.toRadians((i + ((getOptions().getInt("spots") / 6) * 1))
										* (360 / getOptions().getInt("spots")) * (getOptions().getDouble("r"))))),
						loc.getYaw())));
		spawnParticle(particle,
				loc.clone().add(ParticleUtils.rotateAroundAxisY(
						new Vector(
								Math.cos(Math.toRadians((i + ((getOptions().getInt("spots") / 6) * 2))
										* (360 / getOptions().getInt("spots")) * (getOptions().getDouble("r")))),
								1 + Math.cos(Math.toRadians((i + ((getOptions().getInt("spots") / 6) * 2))
										* (360 / getOptions().getInt("spots")) * (getOptions().getDouble("r")))),
								Math.sin(Math.toRadians((i + ((getOptions().getInt("spots") / 6) * 2))
										* (360 / getOptions().getInt("spots")) * (getOptions().getDouble("r"))))),
						loc.getYaw())));
		spawnParticle(particle,
				loc.clone().add(ParticleUtils.rotateAroundAxisY(
						new Vector(
								Math.cos(Math.toRadians((i + ((getOptions().getInt("spots") / 6) * 3))
										* (360 / getOptions().getInt("spots")) * (getOptions().getDouble("r")))),
								1,
								Math.sin(Math.toRadians((i + ((getOptions().getInt("spots") / 6) * 3))
										* (360 / getOptions().getInt("spots")) * (getOptions().getDouble("r"))))),
						loc.getYaw())));

		spawnParticle(particle,
				loc.clone().add(ParticleUtils.rotateAroundAxisY(
						new Vector(
								Math.cos(Math.toRadians((i + ((getOptions().getInt("spots") / 6) * 4))
										* (360 / getOptions().getInt("spots")) * (getOptions().getDouble("r")))),
								1 + ((Math
										.cos(Math.toRadians((i + ((getOptions().getInt("spots") / 6) * 4))
												* (360 / getOptions().getInt("spots")) * (getOptions().getDouble("r"))))
										* -1)),
								Math.sin(Math.toRadians((i + ((getOptions().getInt("spots") / 6) * 4))
										* (360 / getOptions().getInt("spots")) * (getOptions().getDouble("r"))))),
						loc.getYaw())));
		spawnParticle(particle,
				loc.clone().add(ParticleUtils.rotateAroundAxisY(
						new Vector(
								Math.cos(Math.toRadians((i + ((getOptions().getInt("spots") / 6) * 5))
										* (360 / getOptions().getInt("spots")) * (getOptions().getDouble("r")))),
								1 + Math.cos(Math.toRadians((i + ((getOptions().getInt("spots") / 6) * 5))
										* (360 / getOptions().getInt("spots")) * (getOptions().getDouble("r")))),
								Math.sin(Math.toRadians((i + ((getOptions().getInt("spots") / 6) * 5))
										* (360 / getOptions().getInt("spots")) * (getOptions().getDouble("r"))))),
						loc.getYaw())));
		spawnParticle(particle,
				loc.clone().add(ParticleUtils.rotateAroundAxisY(
						new Vector(
								Math.cos(Math.toRadians((i + ((getOptions().getInt("spots") / 6) * 6))
										* (360 / getOptions().getInt("spots")) * (getOptions().getDouble("r")))),
								1,
								Math.sin(Math.toRadians((i + ((getOptions().getInt("spots") / 6) * 6))
										* (360 / getOptions().getInt("spots")) * (getOptions().getDouble("r"))))),
						loc.getYaw())));

	}

}
