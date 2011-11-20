package com.bukkit.gemo.ClashOfKingdoms.game;

import java.io.Serializable;
import java.util.ArrayList;

public class CoKClassSettings implements Serializable
{
	private static final long serialVersionUID = -545675701146458066L;
	private int HelmetId = -1;
	private int ChestPlateId = -1;
	private int LeggingsId = -1;
	private int BootsId = -1;
	private ArrayList<CoKItemStack> ItemList = null;
	
	/////////////////////////////////
	//
	// CONSTRUCTOR
	//
	/////////////////////////////////
	public CoKClassSettings()
	{
		ItemList = new ArrayList<CoKItemStack>();
	}
	
	/////////////////////////////////
	//
	// ADD ITEM
	//
	/////////////////////////////////		
	public void addItem(CoKItemStack item)
	{
		ItemList.add(item);
	}
	
	/////////////////////////////////
	//
	// GETTER & SETTER
	//
	/////////////////////////////////
	public boolean isHelmetSetTo(int Id)
	{
		return this.getHelmetId() == Id;
	}
	
	public boolean isChestPlateSetTo(int Id)
	{
		return this.getChestPlateId() == Id;
	}
	
	public boolean isLeggingsSetTo(int Id)
	{
		return this.getLeggingsId() == Id;
	}
	
	public boolean isBootsSetTo(int Id)
	{
		return this.getBootsId() == Id;
	}
	
	public boolean isHelmetSet()
	{
		return this.getHelmetId() != -1;
	}

	public boolean isChestPlateSet()
	{
		return this.getChestPlateId() != -1;
	}
	
	public boolean isLeggingsSet()
	{
		return this.getLeggingsId() != -1;
	}
	
	public boolean isBootsSet()
	{
		return this.getBootsId() != -1;
	}
	
	public void setHelmetId(int Id)
	{
		HelmetId = Id;
	}

	public void setChestPlateId(int Id)
	{
		ChestPlateId = Id;
	}
	
	public void setLeggingsId(int Id)
	{
		LeggingsId = Id;
	}
	
	public void setBootsId(int Id)
	{
		BootsId = Id;
	}

	/**
	 * @return the itemList
	 */
	public ArrayList<CoKItemStack> getItemList() {
		return ItemList;
	}

	/**
	 * @param itemList the itemList to set
	 */
	public void setItemList(ArrayList<CoKItemStack> itemList) {
		ItemList = itemList;
	}

	/**
	 * @return the helmetId
	 */
	public int getHelmetId() {
		return HelmetId;
	}

	/**
	 * @return the chestPlateId
	 */
	public int getChestPlateId() {
		return ChestPlateId;
	}

	/**
	 * @return the leggingsId
	 */
	public int getLeggingsId() {
		return LeggingsId;
	}

	/**
	 * @return the bootsId
	 */
	public int getBootsId() {
		return BootsId;
	}
}
