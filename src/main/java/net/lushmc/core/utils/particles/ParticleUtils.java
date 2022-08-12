package net.lushmc.core.utils.particles;

import java.awt.Color;

import org.bukkit.util.Vector;

public class ParticleUtils {

	public static Color generateColor(double seed, double frequency) {
		return generateColor(seed, frequency, 100);
	}

	public static Color generateColor(double seed, double frequency, int amp) {

		if (amp > 127)
			amp = 127;
		int peak = 255 - amp;
		int red = (int) (Math.sin(frequency * (seed) + 0) * amp + peak);
		int green = (int) (Math.sin(frequency * (seed) + 2 * Math.PI / 3) * amp + peak);
		int blue = (int) (Math.sin(frequency * (seed) + 4 * Math.PI / 3) * amp + peak);
		if (red > 255)
			red = 255;
		if (green > 255)
			green = 255;
		if (blue > 255)
			blue = 255;

		return new Color(red, green, blue);
	}

	public static Vector rotateAroundAxisX(Vector v, double angle) {
		angle = Math.toRadians(angle);
		double y, z, cos, sin;
		cos = Math.cos(angle);
		sin = Math.sin(angle);
		y = v.getY() * cos - v.getZ() * sin;
		z = v.getY() * sin + v.getZ() * cos;
		return v.setY(y).setZ(z);
	}

	public static Vector rotateAroundAxisY(Vector v, double angle) {
		angle = -angle;
		angle = Math.toRadians(angle);
		double x, z, cos, sin;
		cos = Math.cos(angle);
		sin = Math.sin(angle);
		x = v.getX() * cos + v.getZ() * sin;
		z = v.getX() * -sin + v.getZ() * cos;
		return v.setX(x).setZ(z);
	}

	public static Vector rotateAroundAxisZ(Vector v, double angle) {
		angle = Math.toRadians(angle);
		double x, y, cos, sin;
		cos = Math.cos(angle);
		sin = Math.sin(angle);
		x = v.getX() * cos - v.getY() * sin;
		y = v.getX() * sin + v.getY() * cos;
		return v.setX(x).setY(y);
	}

}
