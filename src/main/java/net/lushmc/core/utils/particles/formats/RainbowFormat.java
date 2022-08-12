package net.lushmc.core.utils.particles.formats;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.lushmc.core.utils.particles.ParticleFormat;
import net.lushmc.core.utils.particles.ParticleUtils;
import net.md_5.bungee.api.ChatColor;

public class RainbowFormat extends ParticleFormat {

	Location cloc = null;
	double r = 1;

	public RainbowFormat() {
		String s = "Rainbow";
//		ComponentBuilder builder = new ComponentBuilder();
		String a = "";
		for (int i = 0; i != s.length(); i++) {
			a = a + ChatColor.of(ParticleUtils.generateColor(i + 2, 0.9, 127)) + s.substring(i, i + 1);
		}

		name = a;
		guiItem = new ItemStack(Material.COAL);
		particle = Particle.REDSTONE;
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

		cloc = loc.clone();

		for (int b = 0; b != 6; b++) {
//			if (b == 0)
//				color = Color.RED;
//			if (b == 1)
//				color = Color.ORANGE;
//			if (b == 2)
//				color = Color.YELLOW;
//			if (b == 3)
//				color = Color.GREEN;
//			if (b == 4)
//				color = Color.BLUE;
//			if (b == 5)
//				color = Color.PURPLE;
//			Color.fromRGB(red, green, blue)
			java.awt.Color color = ParticleUtils.generateColor(b + ((1 * Math.PI) / 2), 1, 127);
			setDustOptions(new DustOptions(Color.fromRGB(color.getRed(), color.getBlue(), color.getGreen()), 0.5F));
			for (int a = 1; a != 21; a++) {
				Vector v = new Vector(0, 2 + Math.cos(Math.toRadians(a + 30) * ((360) / (40))) * (r - (0.1 * b)),
						Math.sin(Math.toRadians(a + 30) * ((360) / (40))) * (r - (0.1 * b)));
				// v = rotateAroundAxisX(v,90);
				v = ParticleUtils.rotateAroundAxisY(v, cloc.getYaw() + 90);
				spawnParticle(particle, loc.clone().add(v));

			}
		}

	}

}
