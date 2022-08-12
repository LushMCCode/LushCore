package net.lushmc.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.inventory.ItemStack;

import net.lushmc.core.utils.particles.ParticleFormat;

public class AngelicFormat extends ParticleFormat {

	public HaloFormat halo = new HaloFormat();
	public WingsFormat wings = new WingsFormat();

	public AngelicFormat() {
		changeParticle = false;
		allowedParticles.add(Particle.REDSTONE);

		name = "&f&lAngelic&f format";
		guiItem = new ItemStack(Material.FEATHER);

		particle = Particle.REDSTONE;
		halo.particle(particle);
		wings.particle(particle);
		halo.setDustOptions(new DustOptions(Color.YELLOW, halo.getOptions().optFloat("size", 1f)));
		wings.setDustOptions(new DustOptions(Color.WHITE, halo.getOptions().optFloat("size", 1f)));
	}

	@Override
	public void particle(Particle particle) {
		super.particle(particle);
		halo.particle(particle);
		wings.particle(particle);
	}

	@Override
	public void setDustOptions(DustOptions dustoptions) {
		super.setDustOptions(dustoptions);
		halo.setDustOptions(dustoptions);
		wings.setDustOptions(dustoptions);
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
		halo.setLifetime(i);
		wings.setLifetime(i);
		halo.display(loc);
		wings.display(loc);
	}

}
