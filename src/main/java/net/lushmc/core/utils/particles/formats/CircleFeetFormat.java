package net.lushmc.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import net.lushmc.core.utils.particles.ParticleFormat;

public class CircleFeetFormat extends ParticleFormat {

	public CircleFeetFormat() {
		changeParticle = true;
		allowedParticles.add(Particle.SPELL_INSTANT);
		allowedParticles.add(Particle.SPELL_MOB);
		allowedParticles.add(Particle.SPELL_WITCH);
		allowedParticles.add(Particle.PORTAL);
		allowedParticles.add(Particle.DAMAGE_INDICATOR);
		allowedParticles.add(Particle.COMPOSTER);
		allowedParticles.add(Particle.FLAME);
		allowedParticles.add(Particle.REDSTONE);

		setOption("spots", 20);
		setOption("cols", 2);

		guiItem = new ItemStack(Material.DIAMOND_BOOTS);
		name = "&7Circle Feet";

	}

	@Override
	public void display(UUID uid) {
		display(Bukkit.getPlayer(uid).getLocation());

	}

	@Override
	public void display(Location loc) {
		if (particle == null)
			return;
//		for (int t = 0; t != getOptions().getInt("cols"); t++) {
//			cloc = loc.clone().add(
//					Math.cos(Math.toRadians(i + ((getOptions().getInt("spots") / getOptions().getInt("cols")) * t))

		for (int t = 0; t != getOptions().getInt("cols"); t++) {
			spawnParticle(particle, loc.clone().add(
					Math.cos(Math.toRadians(((i + ((getOptions().getInt("spots") / getOptions().getInt("cols")) * t))
							* (360 / getOptions().getInt("spots"))))) * (getOptions().getDouble("r")),
					0.1, Math.sin(Math.toRadians(i + ((getOptions().getInt("spots") / getOptions().getInt("cols")) * t))
							* (360 / getOptions().getInt("spots"))) * (getOptions().getDouble("r"))));
		}
//		spawnParticle(particle, loc.clone().add(
//				Math.cos(Math.toRadians(((i) * (360 / getOptions().getInt("spots"))))) * (getOptions().getDouble("r")),
//				0.1,
//				Math.sin(Math.toRadians(i) * (360 / getOptions().getInt("spots"))) * (getOptions().getDouble("r"))));
//		spawnParticle(particle, loc.clone().add(
//				Math.cos(Math
//						.toRadians(((i + (getOptions().getInt("spots") / 2)) * (360 / getOptions().getInt("spots")))))
//						* (getOptions().getDouble("r")),
//				0.1,
//				Math.sin(Math.toRadians(i + (getOptions().getInt("spots") / 2)) * (360 / getOptions().getInt("spots")))
//						* (getOptions().getDouble("r"))));

	}

}
