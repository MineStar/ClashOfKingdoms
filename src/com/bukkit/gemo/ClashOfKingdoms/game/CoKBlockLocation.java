package com.bukkit.gemo.ClashOfKingdoms.game;

import java.io.Serializable;

import org.bukkit.Location;
import org.bukkit.block.Block;
import com.bukkit.gemo.ClashOfKingdoms.*;

public class CoKBlockLocation implements Serializable
{
	private static final long serialVersionUID = -7935448339902183536L;
	
	private String worldName;
	private double x;
	private double y;
	private double z;
	
	// CONSTRUCTOR
	public CoKBlockLocation(Block block)
	{
		Location loc = block.getLocation();
		setWorldName(loc.getWorld().getName());
		setX(loc.getBlockX());
		setY(loc.getBlockY());
		setZ(loc.getBlockZ());
	}
	
	// CONSTRUCTOR
	public CoKBlockLocation(String world, int x, int y, int z)
	{
		setWorldName(world);
		setX(x);
		setY(y);
		setZ(z);
	}	
	
	// RETURN THE BLOCK
	public Block getBlock()
	{
		return CoKCore.server.getWorld(worldName).getBlockAt(getLocation());
	}
	
	// RETURN THE LOCATION
	public Location getLocation()
	{
		return new Location(CoKCore.server.getWorld(worldName), x, y, z);
	}

	////////////////////////////////////
	// GETTER & SETTER
	////////////////////////////////////	
	public String getWorldName() 
	{
		return worldName;
	}

	public void setWorldName(String worldName) 
	{
		this.worldName = worldName;
	}

	public double getX() 
	{
		return x;
	}

	public void setX(double x) 
	{
		this.x = x;
	}

	public double getY() 
	{
		return y;
	}

	public void setY(double y) 
	{
		this.y = y;
	}

	public double getZ() 
	{
		return z;
	}

	public void setZ(double z) 
	{
		this.z = z;
	}
}
