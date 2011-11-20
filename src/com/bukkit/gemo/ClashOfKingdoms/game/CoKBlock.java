package com.bukkit.gemo.ClashOfKingdoms.game;

import java.io.Serializable;

public class CoKBlock implements Serializable
{
	private static final long serialVersionUID = -6946887017494513916L;
	private int Id = 0;
	private byte SubId = 0;
	
	private String worldName;
	private int x;
	private int y;
	private int z;
	
	public CoKBlock(String worldName, int x, int y, int z)
	{
		setWorldName(worldName);
		setX(x);
		setY(y);
		setZ(z);
	}
	
	public CoKBlock(int Id)
	{
		setId(Id);
		setSubId((byte)0);
	}	
	
	public CoKBlock(int Id, byte SubId)
	{
		setId(Id);
		setSubId(SubId);
	}	
	
	public CoKBlock(int Id, byte SubId, String worldName, int x, int y, int z)
	{
		setId(Id);
		setSubId(SubId);
		setWorldName(worldName);
		setX(x);
		setY(y);
		setZ(z);
	}		
		
	/**
	 * @return the id
	 */
	public int getId() {
		return Id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		Id = id;
	}

	/**
	 * @return the subId
	 */
	public byte getSubId() {
		return SubId;
	}

	/**
	 * @param subId the subId to set
	 */
	public void setSubId(byte subId) {
		SubId = subId;
	}

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}	
}
