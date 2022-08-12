package net.lushmc.core.utils.particles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.json2.JSONObject;

import net.lushmc.core.utils.placeholders.Emoticons;

@SuppressWarnings("deprecation")
public class ParticleFormat {

	protected JSONObject options = new JSONObject("{}");

	protected boolean changeParticle = false;
	protected Particle particle = null;
	protected DustOptions dustoptions = new DustOptions(Color.RED, 1);
	protected ItemStack itemstack = new ItemStack(Material.STONE);
	protected BlockData blockdata = null;
	protected MaterialData materialdata = new MaterialData(Material.GOLD_BLOCK);
	protected List<Particle> allowedParticles = new ArrayList<>();
	protected int i = 0;
	private boolean tmp = false;

	protected String name = "Format Name";

	protected ItemStack guiItem = new ItemStack(Material.GRASS_BLOCK);

	public ParticleFormat() {
		options.put("r", 1);
		options.put("h", 2);
		options.put("cols", 2);
		options.put("spots", 40);
		options.put("l", 1.5);
		options.put("size", 1f);
	}

	public void display(UUID uid) throws IllegalArgumentException {
		if (particle == null)
			return;
	}

	public void display(Location loc) throws IllegalArgumentException {
		if (particle == null)
			return;
	}

	public void setLifetime(int i) {
		this.i = i;
	}

	public int getLifetime() {
		return i;
	}

	public void setOption(String key, Object value) {
		options.put(key, value);
	}

	public void loadOptions(JSONObject options) {
		this.options = options == null ? this.options : options;
	}

//	public void setHeight(double h) {
//		this.h = h;
//	}
//
//	public void setRadius(double r) {
//		this.r = r;
//	}
//
//	public void setLength(double l) {
//		this.l = l;
//	}
//
//	public void setSpots(int spots) {
//		this.spots = spots;
//	}
//
//	public void setColumns(int cols) {
//		this.cols = cols;
//	}
//
//	public double getHeight() {
//		return h;
//	}
//
//	public double getCoumns() {
//		return cols;
//	}
//
//	public double getLength() {
//		return l;
//	}
//
//	public int getSpots() {
//		return spots;
//	}

	public void setDustOptions(DustOptions dustoptions) {
		this.dustoptions = dustoptions;
	}

	public void setItemStack(ItemStack itemstack) {
		this.itemstack = itemstack;
	}

	public void setBlockData(BlockData blockdata) {
		this.blockdata = blockdata;
	}

	public void setMaterialData(MaterialData materialdata) {
		this.materialdata = materialdata;
	}

	private Particle convertParticleForBedrock(Player player, Particle particle) {
		if (player.getName().startsWith("[" + Emoticons.WARNING + "]")) {
			switch (particle) {
			case ASH:
				return Particle.SMOKE_NORMAL;
			default:
				return particle;
			}
		}
		return particle;
	}

	public void spawnParticle(Particle particle, Location loc, double offsetX, double offsetY, double offsetZ) {
		if (particle.getDataType() == null) {
			for (Player player : loc.getWorld().getPlayers())
				if (player.getLocation().distance(loc) <= 60)
					player.spawnParticle(convertParticleForBedrock(player, particle), loc, 0, offsetX, offsetY, offsetZ,
							2);
			return;
		}

		if (particle.getDataType() != Void.class) {
			if (particle.getDataType() == DustOptions.class) {
				if (dustoptions.getSize() == 99) {
					tmp = true;
					java.awt.Color color = ParticleUtils.generateColor(i, 0.05125, 127);
					dustoptions = new DustOptions(Color.fromRGB(color.getRed(), color.getBlue(), color.getGreen()),
							options.getFloat("size"));
				}
				if (options.has("color")) {
					try {
						dustoptions = new DustOptions(
								Color.fromRGB((int) (Long.decode(options.getString("color")) + 0L)),
								options.has("size") ? options.getFloat("size") : 1f);
					} catch (NumberFormatException ex) {
						dustoptions = new DustOptions(DyeColor.valueOf(options.getString("color")).getColor(),
								options.has("size") ? options.getFloat("size") : 1f);
					}

				}
				for (Player player : loc.getWorld().getPlayers())
					if (player.getLocation().distance(loc) <= 60)
						player.spawnParticle(convertParticleForBedrock(player, particle), loc, 0, offsetX, offsetY,
								offsetZ, 2, dustoptions);
				if (tmp) {
					tmp = !tmp;
					setDustOptions(new DustOptions(Color.RED, 99));
				}
			}
			if (particle.getDataType() == MaterialData.class)
				for (Player player : loc.getWorld().getPlayers())
					if (player.getLocation().distance(loc) <= 60)
						player.spawnParticle(convertParticleForBedrock(player, particle), loc, 0, offsetX, offsetY,
								offsetZ, 2, materialdata);
			if (particle.getDataType() == ItemStack.class)
				for (Player player : loc.getWorld().getPlayers())
					if (player.getLocation().distance(loc) <= 60)
						player.spawnParticle(convertParticleForBedrock(player, particle), loc, 0, offsetX, offsetY,
								offsetZ, 2, itemstack);
			if (particle.getDataType() == BlockData.class)
				for (Player player : loc.getWorld().getPlayers())
					if (player.getLocation().distance(loc) <= 60)
						player.spawnParticle(convertParticleForBedrock(player, particle), loc, 0, offsetX, offsetY,
								offsetZ, 2, blockdata);
		} else {
			for (Player player : loc.getWorld().getPlayers())
				if (player.getLocation().distance(loc) <= 60)
					player.spawnParticle(convertParticleForBedrock(player, particle), loc, 0, offsetX, offsetY, offsetZ,
							2);
		}

	}

	public void spawnParticle(UUID uid, Particle particle, Location loc, double offsetX, double offsetY,
			double offsetZ) {
		if (Bukkit.getPlayer(uid) == null)
			return;
		spawnParticle(particle, loc, offsetX, offsetY, offsetZ);

	}

	public void spawnParticle(UUID uid, Particle particle, Location loc) {
		if (Bukkit.getPlayer(uid) == null)
			return;
		spawnParticle(particle, loc);
	}

	public void spawnParticle(Particle particle, Location loc) {
		spawnParticle(particle, loc, 0, 0, 0);
	}

	public List<Particle> allowedParticles() {
		return allowedParticles;
	}

	public ItemStack item() {
		return guiItem;
	}

	public String name() {
		return name;
	}

	public void particle(Particle particle) {
		this.particle = particle;
	}

	public Particle particle() {
		return particle;
	}

	public boolean changeParticle() {
		return changeParticle;
	}

	public JSONObject getOptions() {
		return options;
	}

}
