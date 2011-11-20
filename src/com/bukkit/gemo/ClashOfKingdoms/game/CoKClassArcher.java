package com.bukkit.gemo.ClashOfKingdoms.game;

public class CoKClassArcher extends CoKClass
{
	///////////////////////////////////
	//
	// CONSTRUCTOR
	//
	///////////////////////////////////
	public CoKClassArcher(String ClassName)
	{
		super(ClassName);
		this.setShowMSG(true);
	}
	
	// GET PUNISHMENT
	public int getPunishment(int blockcount)
	{
		return (int) (blockcount/2);
	}
}
