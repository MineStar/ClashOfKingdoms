package com.bukkit.gemo.ClashOfKingdoms.game;

import java.io.Serializable;
import org.bukkit.Location;
import com.bukkit.gemo.ClashOfKingdoms.*;

public class CoKLocation implements Serializable
{
	private static final long serialVersionUID = -303169915896365788L;
	private String worldName;
	private double x;
	private double y;
	private double z;
	private float pitch;
	private float yaw;
	
	// CONSTRUCTOR
	public CoKLocation(Location loc)
	{
		setWorldName(loc.getWorld().getName());
		setX(loc.getX());
		setY(loc.getY());
		setZ(loc.getZ());
		setPitch(loc.getPitch());
		setYaw(loc.getYaw());
	}
	
	// RETURN THE LOCATION
	public Location getLocation()
	{
		if(worldName == null)
			return null;
		
		if(CoKCore.server.getWorld(worldName) == null)
			return null;
		
		Location loc = new Location(CoKCore.server.getWorld(worldName), x, y, z);
		loc.setPitch(pitch);
		loc.setYaw(yaw);
		return loc;
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

	public float getPitch() 
	{
		return pitch;
	}

	public void setPitch(float pitch) 
	{
		this.pitch = pitch;
	}

	public float getYaw() 
	{
		return yaw;
	}

	public void setYaw(float yaw) 
	{
		this.yaw = yaw;
	}
}
