package net.lushmc.core.utils.particles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.lushmc.core.utils.particles.formats.AngelicFormat;
import net.lushmc.core.utils.particles.formats.AtomicFormat;
import net.lushmc.core.utils.particles.formats.CapeFormat;
import net.lushmc.core.utils.particles.formats.CircleFeetFormat;
import net.lushmc.core.utils.particles.formats.CircleHeadFormat;
import net.lushmc.core.utils.particles.formats.DotFormat;
import net.lushmc.core.utils.particles.formats.DoubleHelixFormat;
import net.lushmc.core.utils.particles.formats.GemsFormat;
import net.lushmc.core.utils.particles.formats.HaloFormat;
import net.lushmc.core.utils.particles.formats.HatFormat;
import net.lushmc.core.utils.particles.formats.HelixFormat;
import net.lushmc.core.utils.particles.formats.LilyPadFormat;
import net.lushmc.core.utils.particles.formats.PopperFormat;
import net.lushmc.core.utils.particles.formats.RainCloudFormat;
import net.lushmc.core.utils.particles.formats.RainbowFormat;
import net.lushmc.core.utils.particles.formats.RandomFormat;
import net.lushmc.core.utils.particles.formats.SelectorFormat;
import net.lushmc.core.utils.particles.formats.WingsFormat;

public enum ParticleFormatEnum {

	CIRCLE_HEAD("Circle Head"), CIRCLE_FEET("Circle Feet"), HALO("Halo"), HAT("Hat"), HELIX("Helix"),
	DOUBLE_HELIX("Double Helix"), RANDOM("Random"), CAPE("Cape"), POPPER("Popper", false), RAINBOW("Rainbow"),
	LILY_PAD("Lily Pad"), RAIN_CLOUD("Rain Cloud"), ATOMIC("Atomic"), SELECTOR("Selector"), GEMS("Gems"),
	ANGELIC("Angelic", true), WINGS("Wings", false), DOT("Dot", false);

	String name;
	boolean avalible = true;

	ParticleFormat formatter;

	ParticleFormatEnum(String name) {
		this.name = name;
		formatter = formatter();
	}

	ParticleFormatEnum(String name, boolean avalible) {
		this.name = name;
		this.avalible = avalible;
		formatter = formatter();
	}

	public static List<ParticleFormatEnum> getAvalibleFormats(UUID uid) {
		List<ParticleFormatEnum> formats = new ArrayList<>();
		for (ParticleFormatEnum format : ParticleFormatEnum.values()) {
			if (format.isAvalible()) {
				formats.add(format);
			} 
		}
		return formats;
	}

	public boolean isAvalible() {
		return avalible;
	}

	@Override
	public String toString() {
		return name();
	}

	public static ParticleFormatEnum enumName(ParticleFormat format) {
		if (format instanceof CircleHeadFormat)
			return CIRCLE_HEAD;
		if (format instanceof CircleFeetFormat)
			return CIRCLE_FEET;
		if (format instanceof HelixFormat)
			return HELIX;
		if (format instanceof HaloFormat)
			return HALO;
		if (format instanceof RandomFormat)
			return RANDOM;
		if (format instanceof LilyPadFormat)
			return LILY_PAD;
		if (format instanceof HatFormat)
			return HAT;
		if (format instanceof CapeFormat)
			return CAPE;
		if (format instanceof RainbowFormat)
			return RAINBOW;
		if (format instanceof RainCloudFormat)
			return RAIN_CLOUD;
		if (format instanceof AtomicFormat)
			return ATOMIC;
		if (format instanceof DoubleHelixFormat)
			return DOUBLE_HELIX;
		if (format instanceof SelectorFormat)
			return SELECTOR;
		if (format instanceof GemsFormat)
			return GEMS;
		if (format instanceof PopperFormat)
			return POPPER;
		if (format instanceof WingsFormat)
			return WINGS;
		if (format instanceof AngelicFormat)
			return ANGELIC;
		if (format instanceof DotFormat)
			return DOT;
		return CIRCLE_FEET;
	}

	public ParticleFormat formatter() {

		switch (name) {
		case "Circle Head":
			return new CircleHeadFormat();
		case "Circle Feet":
			return new CircleFeetFormat();
		case "Helix":
			return new HelixFormat();
		case "Halo":
			return new HaloFormat();
		case "Random":
			return new RandomFormat();
		case "Lily Pad":
			return new LilyPadFormat();
		case "Hat":
			return new HatFormat();
		case "Cape":
			return new CapeFormat();
		case "Rainbow":
			return new RainbowFormat();
		case "Rain Cloud":
			return new RainCloudFormat();
		case "Atomic":
			return new AtomicFormat();
		case "Double Helix":
			return new DoubleHelixFormat();
		case "Selector":
			return new SelectorFormat();
		case "Gems":
			return new GemsFormat();
		case "Popper":
			return new PopperFormat();
		case "Wings":
			return new WingsFormat();
		case "Angelic":
			return new AngelicFormat();
		case "Dot":
			return new DotFormat();
		default:
			return new CircleHeadFormat();

		}
	}

}
