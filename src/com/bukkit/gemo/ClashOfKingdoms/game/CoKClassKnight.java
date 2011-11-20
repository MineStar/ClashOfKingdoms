package com.bukkit.gemo.ClashOfKingdoms.game;

public class CoKClassKnight extends CoKClass
{
	///////////////////////////////////
	//
	// CONSTRUCTOR
	//
	///////////////////////////////////
	public CoKClassKnight(String ClassName)
	{
		super(ClassName);
		this.setShowMSG(false);
	}
	
	// GET PUNISHMENT
	public int getPunishment(int blockcount)
	{
		return 2;
	}
}
