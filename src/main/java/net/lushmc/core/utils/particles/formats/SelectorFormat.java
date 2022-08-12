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

public class SelectorFormat extends ParticleFormat {
	Location loc = null;

	RandomFormat random = new RandomFormat();

	public SelectorFormat() {
		changeParticle = true;
		setOption("cols", 5);
		allowedParticles.add(Particle.COMPOSTER);
		allowedParticles.add(Particle.DOLPHIN);
		allowedParticles.add(Particle.FALLING_WATER);
		allowedParticles.add(Particle.NOTE);
		allowedParticles.add(Particle.END_ROD);
		allowedParticles.add(Particle.FLAME);
		allowedParticles.add(Particle.REDSTONE);

		name = "&6Selector";
		guiItem = new ItemStack(Material.BLAZE_POWDER);
	}

	@Override
	public void display(UUID uid) {

		display(Bukkit.getPlayer(uid).getLocation());
	}

	@Override
	public void display(Location loc) {
		if (particle == null)
			return;
		random.particle(particle);
		random.setBlockData(blockdata);
		random.setDustOptions(dustoptions);
		random.setMaterialData(materialdata);
		random.setOption("l", getOptions().get("l"));
		random.setOption("h", getOptions().get("h"));
		this.loc = loc;
		random.setLifetime(i);
		random.display(loc);
		for (int t = 0; t != getOptions().getInt("cols"); t++) {
			spawnParticle(particle, loc.clone().add(ParticleUtils.rotateAroundAxisY(new Vector(
					Math.cos(Math.toRadians(t) * (360 / getOptions().getInt("cols"))) * (getOptions().getDouble("l")),
					0.05,
					Math.sin(Math.toRadians(t) * (360 / getOptions().getInt("cols"))) * (getOptions().getDouble("l"))),
					i * 2)));
		}

	}

}
