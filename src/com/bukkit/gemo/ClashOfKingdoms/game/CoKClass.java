package com.bukkit.gemo.ClashOfKingdoms.game;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CoKClass
{
	private String ClassName = "";
	
	private int HelmId = -1;
	private int ChestPlateId = -1;
	private int LeggingsId = -1;
	private int BootsId = -1;
	private ArrayList<ItemStack> ItemList;
	private boolean showMSG = true;
	
	///////////////////////////////////
	//
	// CONSTRUCTOR
	//
	///////////////////////////////////
	public CoKClass(String ClassName)
	{
		this.ClassName = ClassName;
		ItemList = new ArrayList<ItemStack>();
	}
	
	// GET PUNISHMENT
	public int getPunishment(int blockcount)
	{
		return 0;
	}
	
	///////////////////////////////////
	//
	// ITEM METHODS
	//
	///////////////////////////////////
	
	// ADD ITEM TO LIST
	public void addItem(ItemStack item)
	{
		ItemList.add(item);
	}
	
	// GIVE ITEMS TO PLAYER
	public void giveKit(Player player)
	{
		if(player == null)
			return;
		
		player.getInventory().setHelmet(getHelm());
		player.getInventory().setChestplate(getChestPlate());
		player.getInventory().setLeggings(getLeggings());
		player.getInventory().setBoots(getBoots());
		
		for(ItemStack item : ItemList)
		{
			player.getInventory().setItem(player.getInventory().firstEmpty(), item);
		}
	}
	
	// CLEAR ITEMKIT
	public void clearKit(Player player)
	{
		if(player == null)
			return;
		
		if(getHelm() != null)
			player.getInventory().setHelmet(null);
		if(getChestPlate() != null)
			player.getInventory().setChestplate(null);
		if(getLeggings() != null)
			player.getInventory().setLeggings(null);
		if(getBoots() != null)
			player.getInventory().setBoots(null);
		
		Inventory inv = player.getInventory();
		for(int i = 0; i < inv.getSize(); i++)
		{
			for(ItemStack item : ItemList)
			{			
				if(inv.getItem(i).getTypeId() == item.getTypeId())
				{
					inv.clear(i);
				}
				else if(getHelm() != null)
				{
					if(inv.getItem(i).getTypeId() == this.getHelm().getTypeId())
					{
						inv.clear(i);
					}
				}
				else if(getChestPlate() != null)
				{
					if(inv.getItem(i).getTypeId() == this.getChestPlate().getTypeId())
					{
						inv.clear(i);
					}
				}				
				else if(getLeggings() != null)
				{
					if(inv.getItem(i).getTypeId() == this.getLeggings().getTypeId())
					{
						inv.clear(i);
					}
				}
				else if(getBoots() != null)
				{
					if(inv.getItem(i).getTypeId() == this.getBoots().getTypeId())
					{
						inv.clear(i);
					}
				}
			}
		}
	}
	
	// CAN DROP ITEM
	public boolean canDrop(ItemStack item)
	{
		if(getHelm() != null)
			if(getHelm().getTypeId() == item.getTypeId())
				return false;
		if(getChestPlate() != null)
			if(getChestPlate().getTypeId() == item.getTypeId())
				return false;
		if(getLeggings() != null)
			if(getLeggings().getTypeId() == item.getTypeId())
				return false;
		if(getBoots() != null)
			if(getBoots().getTypeId() == item.getTypeId())
				return false;
		
		for(ItemStack thisItem : ItemList)
		{
			if(thisItem.getTypeId() == item.getTypeId())
				return false;
		}
		
		return true;
	}
	
	///////////////////////////////////
	//
	// GETTER & SETTER
	//
	///////////////////////////////////
	/**
	 * @return the className
	 */
	public String getClassName() 
	{
		return ClassName;
	}
	
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) 
	{
		ClassName = className;
	}
	
	/**
	 * @return the helm
	 */
	public ItemStack getHelm() 
	{
		if(HelmId == -1)
			return null;
		return new ItemStack(Material.getMaterial(HelmId), 1);
	}
	
	/**
	 * @param helmId the helmId to set
	 */
	public void setHelmId(int helmId) 
	{
		HelmId = helmId;
	}
	
	/**
	 * @return the chestPlate
	 */
	public ItemStack getChestPlate()
	{
		if(ChestPlateId == -1)
			return null;
		return new ItemStack(Material.getMaterial(ChestPlateId), 1);
	}
	
	/**
	 * @param chestPlateId the chestPlateId to set
	 */
	public void setChestPlateId(int chestPlateId)
	{
		ChestPlateId = chestPlateId;
	}
	
	/**
	 * @return the leggings
	 */
	public ItemStack getLeggings()
	{
		if(LeggingsId == -1)
			return null;
		return new ItemStack(Material.getMaterial(LeggingsId));
	}
	
	/**
	 * @param leggingsId the leggingsId to set
	 */
	public void setLeggingsId(int leggingsId)
	{
		LeggingsId = leggingsId;
	}
	
	/**
	 * @return the boots
	 */
	public ItemStack getBoots()
	{
		if(BootsId == -1)
			return null;
		return new ItemStack(Material.getMaterial(BootsId));
	}
	
	/**
	 * @param bootsId the bootsId to set
	 */
	public void setBootsId(int bootsId)
	{
		BootsId = bootsId;
	}

	public void setShowMSG(boolean showMSG) 
	{
		this.showMSG = showMSG;
	}

	public boolean isShowMSG()
	{
		return showMSG;
	}
	

}
