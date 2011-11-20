package com.bukkit.gemo.ClashOfKingdoms.game;

import java.io.Serializable;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class CoKGameSettings implements Serializable
{
	private static final long serialVersionUID = 6838470340375825699L;
	
	// GENERAL SETTINGS
	private String settingsName = "default";
	private int towerHeight = 3;
	private int towerID = 1;
	private byte towerData = 0;
		
	// SPAWNPOINTS
	private CoKLocation spawnRed = null;
	private CoKLocation spawnBlu = null;
	private CoKLocation spawnRef = null;
	private CoKLocation spawnPun = null;	
	
	// CLASS SETTINGS
	/** LEADER */
	private boolean LeaderAllowed = true;
	private CoKClassSettings LeaderSettings = null;
	
	/** ARCHER */
	private int ArcherArrowCount = 64;
	private boolean ArcherAllowed = true;
	private CoKClassSettings ArcherSettings = null;
	
	/** KNIGHT */
	private boolean KnightAllowed = true;
	private CoKClassSettings KnightSettings = null;
		
	// GAME SETTINGS	
	private HashMap<String, CoKBlockLocation> RedPlate;
	private HashMap<String, CoKBlockLocation> BluPlate;
	
	///////////////////////////////////
	//
	// CONSTRUCTOR
	//
	///////////////////////////////////
	public CoKGameSettings(String settingsName)
	{
		this.setSettingsName(settingsName);
		RedPlate = new HashMap<String, CoKBlockLocation>();
		BluPlate = new HashMap<String, CoKBlockLocation>();
		this.initLeaderSettings();
		this.initArcherSettings();
		this.initKnightSettings();
	}
	
	///////////////////////////////////
	//
	// CLASSSETTINGS
	//
	///////////////////////////////////
	public void initLeaderSettings()
	{
		LeaderSettings = new CoKClassSettings();
		LeaderSettings.setHelmetId(Material.GOLD_HELMET.getId());
		LeaderSettings.setChestPlateId(Material.DIAMOND_CHESTPLATE.getId());
		LeaderSettings.setLeggingsId(Material.DIAMOND_LEGGINGS.getId());
		LeaderSettings.setBootsId(Material.DIAMOND_BOOTS.getId());
		LeaderSettings.addItem(new CoKItemStack(Material.DIAMOND_SWORD.getId()));
	}
	
	public void initArcherSettings()
	{
		ArcherSettings = new CoKClassSettings();
		ArcherSettings.setHelmetId(Material.LEATHER_HELMET.getId());
		ArcherSettings.addItem(new CoKItemStack(Material.BOW.getId()));
		ArcherSettings.addItem(new CoKItemStack(Material.ARROW.getId(), ArcherArrowCount));		
	}	
	
	public void initKnightSettings()
	{
		KnightSettings = new CoKClassSettings();
		KnightSettings.addItem(new CoKItemStack(Material.IRON_SWORD.getId()));		
	}		
	
	///////////////////////////////////
	//
	// SETTINGS
	//
	///////////////////////////////////	
	// IS SETTING COMPLETE
	public boolean isComplete()
	{
		if(towerHeight < 1)
			return false;
		if(getSpawnRed() == null)
			return false;
		if(getSpawnBlu() == null)
			return false;
		if(getSpawnRef() == null)
			return false;
		if(getSpawnPun() == null)
			return false;
		return true;
	}
	
	// SET DATA
	public void setData(HashMap<String, Block> T1, HashMap<String, Block> T2)
	{
		RedPlate = new HashMap<String, CoKBlockLocation>();
		BluPlate = new HashMap<String, CoKBlockLocation>();
		
		for(Block block : T1.values())
		{
			RedPlate.put(block.getLocation().toString(), new CoKBlockLocation(block));
		}
		for(Block block : T2.values())
		{
			BluPlate.put(block.getLocation().toString(), new CoKBlockLocation(block));
		}
	}	
	
	////////////////////////////////////
	// GETTER & SETTER
	////////////////////////////////////	
	public void setSettingsName(String settingsName)
	{
		this.settingsName = settingsName;
	}

	public String getSettingsName()
	{
		return settingsName;
	}

	public CoKLocation getSpawnRed() 
	{
		return spawnRed;
	}

	public void setSpawnRed(CoKLocation spawnTeamRED) 
	{
		this.spawnRed = spawnTeamRED;
	}

	public CoKLocation getSpawnBlu()
	{
		return spawnBlu;
	}

	public void setSpawnBlu(CoKLocation spawnTeamBLUE) 
	{
		this.spawnBlu = spawnTeamBLUE;
	}

	public CoKLocation getSpawnRef() 
	{
		return spawnRef;
	}

	public void setSpawnRef(CoKLocation spawnReferees) 
	{
		this.spawnRef = spawnReferees;
	}

	public CoKLocation getSpawnPun() 
	{
		return spawnPun;
	}

	public void setSpawnPun(CoKLocation spawnPunishment) 
	{
		this.spawnPun = spawnPunishment;
	}
	
	public HashMap<String, CoKBlockLocation> getRedPlate()
	{
		return RedPlate;
	}
	
	public void setRedPlate(HashMap<String, CoKBlockLocation> RedPlate) 
	{
		this.RedPlate = RedPlate;
	}
	
	public HashMap<String, CoKBlockLocation> getBluPlate() 
	{
		return BluPlate;
	}
	
	public void setBluPlate(HashMap<String, CoKBlockLocation> BluPlate) 
	{
		this.BluPlate = BluPlate;
	}
	
	public int getTowerHeight() 
	{
		return towerHeight;
	}
	
	public void setTowerHeight(int towerheight) 
	{
		this.towerHeight = towerheight;
	}
	
	public int getTowerID() 
	{
		return towerID;
	}
	
	public void setTowerID(int towerID) 
	{
		this.towerID = towerID;
	}
	
	public byte getTowerData() 
	{
		return towerData;
	}	

	public void setTowerData(byte towerData) 
	{
		this.towerData = towerData;
	}

	public int getArcherArrowCount() 
	{
		return ArcherArrowCount;
	}

	public void setArcherArrowCount(int archerArrowCount)
	{
		ArcherArrowCount = archerArrowCount;
	}

	public boolean isArcherAllowed()
	{
		return ArcherAllowed;
	}

	public void setArcherAllowed(boolean archerAllowed)
	{
		ArcherAllowed = archerAllowed;
	}	
	
	public boolean isKnightAllowed()
	{
		return KnightAllowed;
	}

	public void setKnightAllowed(boolean knightAllowed)
	{
		KnightAllowed = knightAllowed;
	}

	public boolean isLeaderAllowed() 
	{
		return LeaderAllowed;
	}

	public void setLeaderAllowed(boolean leaderAllowed) 
	{
		LeaderAllowed = leaderAllowed;
	}

	
	public CoKClassSettings getLeaderSettings() 
	{
		return LeaderSettings;
	}
	

	public CoKClassSettings getArcherSettings()
	{
		return ArcherSettings;
	}

	public CoKClassSettings getKnightSettings() 
	{
		return KnightSettings;
	}
}
