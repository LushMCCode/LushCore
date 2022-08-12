package net.lushmc.core.utils.particles.formats;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.lushmc.core.utils.particles.ParticleFormat;

public class RandomFormat extends ParticleFormat {

	public RandomFormat() {
		changeParticle = true;
		allowedParticles.add(Particle.SPELL_INSTANT);
		allowedParticles.add(Particle.SPELL_MOB);
		allowedParticles.add(Particle.SPELL_WITCH);
		allowedParticles.add(Particle.PORTAL);
		allowedParticles.add(Particle.DAMAGE_INDICATOR);
		allowedParticles.add(Particle.COMPOSTER);
		allowedParticles.add(Particle.FLAME);
		allowedParticles.add(Particle.REDSTONE);

		guiItem = new ItemStack(Material.IRON_SWORD);
		name = "&bRandom";

	}

	@Override
	public void display(UUID uid) {
		display(Bukkit.getPlayer(uid).getLocation());
//		if(particle!=null)spawnParticle(uid, particle,
//				Bukkit.getPlayer(uid).getLocation().clone().add(-0.75 + (CoreUtils.getRandom().nextDouble()*1.5),
//						(1.5 + CoreUtils.getRandom().nextDouble())
//								- (CoreUtils.getRandom().nextInt(2) + CoreUtils.getRandom().nextDouble()),
//						-0.75 + (CoreUtils.getRandom().nextDouble()*1.5)));
	}

	@Override
	public void display(Location loc) {
		if (particle != null)
			spawnParticle(particle, loc.clone().add(-0.75 + (new Random().nextDouble() * 1.5),
					(getOptions().getDouble("l") + new Random().nextDouble())
							- (new Random().nextInt((int) getOptions().getDouble("h")) + new Random().nextDouble()),
					-(getOptions().getDouble("l") / 2) + (new Random().nextDouble() * getOptions().getDouble("l"))));
	}

}
