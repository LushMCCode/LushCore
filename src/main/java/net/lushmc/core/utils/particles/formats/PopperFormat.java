package net.lushmc.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.lushmc.core.utils.particles.ParticleFormat;

public class PopperFormat extends ParticleFormat {
	Location loc = null;
	int colspots = 180;

	public PopperFormat() {
		changeParticle = true;

		allowedParticles.add(Particle.COMPOSTER);
		allowedParticles.add(Particle.DOLPHIN);
		allowedParticles.add(Particle.FALLING_WATER);
		allowedParticles.add(Particle.NOTE);
		allowedParticles.add(Particle.END_ROD);
		allowedParticles.add(Particle.REDSTONE);

		name = "&aPopper";
		guiItem = new ItemStack(Material.GUNPOWDER);

		setOption("spots", 40);
		setOption("r", 1);
		setOption("cols", 4);
	}

	@Override
	public void display(UUID uid) {
		super.display(uid);
		if (particle == null)
			return;
		for (int t = 0; t != getOptions().getInt("cols"); t++) {
			double r = Math
					.sin(Math.toRadians(((i + ((getOptions().optInt("spots2", 10) / getOptions().getInt("cols")) * t))
							* (360 / getOptions().optInt("spots2", 10)))));
			double y = Math.cos(Math.toRadians(((i) * (360 / colspots)))) * (getOptions().getDouble("h"));
			spawnParticle(particle, loc.clone().add(
					Math.cos(Math.toRadians(((i + ((getOptions().getInt("spots") / getOptions().getInt("cols")) * t))
							* (360 / getOptions().getInt("spots"))))) * (r),
					y, Math.sin(Math.toRadians(i + ((getOptions().getInt("spots") / getOptions().getInt("cols")) * t))
							* (360 / getOptions().getInt("spots"))) * (r)));
		}
//		for(int t=0;t!=cols;t++){
//			loc = Bukkit.getPlayer(uid).getLocation().add(
//					Math.cos(Math.toRadians(i + ((spots/cols)*t)) * (360 / spots) * (r)),
//					1 + Math.cos(Math.toRadians(((i) * (360 / colspots)) * (r*2))),
//					Math.sin(Math.toRadians(i + ((spots/cols)*t)) * (360 / spots)) * (r));
//			spawnParticle(uid,particle,loc);
//		}

	}

}
