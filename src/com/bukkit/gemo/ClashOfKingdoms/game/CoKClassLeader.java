package com.bukkit.gemo.ClashOfKingdoms.game;

public class CoKClassLeader extends CoKClass
{
	///////////////////////////////////
	//
	// CONSTRUCTOR
	//
	///////////////////////////////////
	public CoKClassLeader(String ClassName)
	{
		super(ClassName);
		this.setShowMSG(true);
	}
	
	// GET PUNISHMENT
	public int getPunishment(int blockcount)
	{
		return blockcount;
	}
}
