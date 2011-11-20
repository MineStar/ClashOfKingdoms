package com.bukkit.gemo.ClashOfKingdoms.game;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.bukkit.Location;

public class CoKSelectArea
{
	private String worldName;
	private ArrayList<Location> locList;
	
	public CoKSelectArea(String worldName)
	{
		locList = new ArrayList<Location>();
		this.worldName = worldName;
	}
	
	public int getSize()
	{
		return locList.size();
	}
	
	public boolean notEmpty()
	{
		return locList.size() > 0;
	}
	
	public void add(Location loc)
	{
		locList.add(loc);
	}
	
	public String getWorldName()
	{
		return this.worldName;
	}
	
	public ArrayList<Point2D> getPointList()
	{
		ArrayList<Point2D> result = new ArrayList<Point2D>();
		for(Location loc : locList)
		{
			result.add(new Point(loc.getBlockX(), loc.getBlockZ()));
		}		
		return result;
	}

	public ArrayList<Location> getLocList() {
		return locList;
	}
}
