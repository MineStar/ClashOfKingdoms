package com.bukkit.gemo.ClashOfKingdoms.game;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import com.bukkit.gemo.ClashOfKingdoms.*;

public class CoKGame
{
	public String gameName = "";
	public String gameOwner = "";
	public boolean gameOn = false;
	public boolean gamePaused = false;
	
	private CoKClass LeaderClass, ArcherClass, KnightClass;	
	private HashMap<EnumTeam, HashMap<String, String>> ClassPlayers;
		
	public HashMap<String, Integer> PlayerList = new HashMap<String, Integer>();
	public HashMap<String, Integer> TeamRed = new HashMap<String, Integer>();
	public HashMap<String, Integer> TeamBlu = new HashMap<String, Integer>();
	public HashMap<String, Integer> TeamRef = new HashMap<String, Integer>();
	public HashMap<String, Integer> TeamPun = new HashMap<String, Integer>();
	
	public HashMap<String, Block> RedPlate = new HashMap<String, Block>();
	public HashMap<String, Block> BluPlate = new HashMap<String, Block>();
	
	public CoKGameSettings currentSettings = null;	
		
	//////////////////////////////
	//
	// CONSTRUCTOR
	//
	//////////////////////////////
	public CoKGame(String gameName, String owner)
	{
		this.gameName = gameName;
		this.gameOwner = owner;
		this.currentSettings = new CoKGameSettings("default");
	}
	
	//////////////////////////////
	//
	// GAME METHODS
	//
	//////////////////////////////
	
	// GET MINIMUM PLAYERCOUNT
	public int getMinPlayerCount()
	{
		int min = 1;
		if(currentSettings.isLeaderAllowed())
			min++;
		if(currentSettings.isArcherAllowed())
			min++;
		if(currentSettings.isKnightAllowed())
			min++;
		return min;
	}
	
	// CHECK GAMESTATE
	public void checkGameState()
	{
		// CHECK IF TOWER IS COMPLETE
		if(isTowerComplete(EnumTeam.RED) && !isTowerComplete(EnumTeam.BLU))
		{
			sendMessageToAll(ChatColor.AQUA + "THE GAMES HAVE A WINNER!");	
			sendMessageToAll(getTeamString(EnumTeam.BLU) + " has won the round!");
			setGameOn(false);
		}
		else if(isTowerComplete(EnumTeam.BLU) && !isTowerComplete(EnumTeam.RED))
		{
			sendMessageToAll(ChatColor.AQUA + "THE GAMES HAVE A WINNER!");			
			sendMessageToAll(getTeamString(EnumTeam.RED) + " has won the round!");
			setGameOn(false);
		}
		else if(isTowerComplete(EnumTeam.RED) && isTowerComplete(EnumTeam.BLU))
		{
			sendMessageToAll(ChatColor.AQUA + "THE GAMES HAVE ENDED!");
			sendMessageToAll(ChatColor.WHITE + "NO TEAM" + ChatColor.GOLD + " has won the round! (DRAW)");
			setGameOn(false);
		}
	}
	
	// START GAME
	public void startGame()
	{
		if(TeamRed.values().size() >= getMinPlayerCount() && TeamBlu.values().size() >= getMinPlayerCount())
		{
			initClasses();			
			this.gamePaused = false;			
			this.sendMessageToAll(ChatColor.AQUA + "LET THE GAMES BEGIN!");
			this.setGameOn(true);
			this.callNewLeader(EnumTeam.RED);
			this.callNewLeader(EnumTeam.BLU);
			this.callNewArcher(EnumTeam.RED);
			this.callNewArcher(EnumTeam.BLU);
			this.callNewKnight(EnumTeam.RED);
			this.callNewKnight(EnumTeam.BLU);
		}
		else
		{
			this.sendMessageToAll(ChatColor.RED + "Each team must have at least " + getMinPlayerCount() + " members!");
		}
	}
	
	// IS GAME PAUSED
	public boolean isGamePaused() 
	{
		return gamePaused;
	}

	// TOGGLE PAUSE
	public void toggleGamePaused()
	{	
		this.gamePaused = !this.isGamePaused();
		if(isGamePaused())
		{
			this.sendMessageToAll(ChatColor.AQUA + "THE GAME IS NOW PAUSED!");
		}
		else
		{
			this.sendMessageToAll(ChatColor.AQUA + "LET THE GAMES GO ON!");	
		}
	}
	
	// SET PAUSE
	public void setGamePaused(boolean paused)
	{	
		this.gamePaused = paused;
		if(isGamePaused())
		{
			this.sendMessageToAll(ChatColor.AQUA + "THE GAME IS NOW PAUSED!");
		}
		else
		{
			this.sendMessageToAll(ChatColor.AQUA + "LET THE GAMES GO ON!");	
		}
	}	
	
	// STOP GAME
	public void stopGame()
	{
		this.gamePaused = false;
		this.sendMessageToAll(ChatColor.GOLD + "The game was stopped by a referee!");
		this.setGameOn(false);
	}
	
	// STOP GAME WITH REASON
	public void stopGame(String reason)
	{
		this.gamePaused = false;
		this.sendMessageToAll(ChatColor.GOLD + "The game was stopped automaticly.");
		this.sendMessageToAll(ChatColor.GRAY + "REASON: " + reason);	
		this.setGameOn(false);
	}	
	
	// SHOW SCORE
	public void showScore()
	{
		int scoreRed = getScore(EnumTeam.BLU);
		int scoreBlu = getScore(EnumTeam.RED);
		int maxBlocks = currentSettings.getTowerHeight() * RedPlate.values().size();
		
		this.sendMessageToAll(getTeamString(EnumTeam.RED) + " placed " + scoreRed + " of " + maxBlocks + " blocks!");
		this.sendMessageToAll(getTeamString(EnumTeam.BLU) + " placed " + scoreBlu + " of " + maxBlocks + " blocks!");
	}
	
	// IS TOWER COMPLETE
	public boolean isTowerComplete(EnumTeam TeamID)
	{
		if(TeamID == EnumTeam.RED)
		{
			if(getScore(TeamID) == currentSettings.getTowerHeight() * RedPlate.values().size())
			{
				return true;
			}			
			return false;
		}
		else if(TeamID == EnumTeam.BLU)
		{
			if(getScore(TeamID) == currentSettings.getTowerHeight() * BluPlate.values().size())
			{
				return true;
			}			
			return false;
		}
		return false;
	}
	
	// GET SCORE
	public int getScore(final EnumTeam TeamID)
	{
		HashMap<String, Block> curPlate;
		switch(TeamID)
		{
			case RED:
			{
				// RED
				curPlate = RedPlate;
				break;
			}
			case BLU:
			{
				// BLU
				curPlate = BluPlate;
				break;
			}
			default:
			{
				// NONE
				return -1;
			}
		}
		
		int count = 0;
		for(Block block : curPlate.values())
		{
			for(int i = 1; i <= currentSettings.getTowerHeight(); i++)
			{
				if(block.getRelative(0, i, 0).getTypeId() == currentSettings.getTowerID()
						&& block.getRelative(0, i, 0).getData() == currentSettings.getTowerData())
				{
					count++;
				}
			}
		}
		curPlate = null;
		return count;
	}
	
	// TELEPORT TEAM TO BASE
	public void teleportTeamToBase(final EnumTeam TeamID)
	{
		Player player;
		for(String name : this.getTeamArray(TeamID))
		{
			player = CoKCore.getPlayer(name);
			if(player == null)
				continue;
			
			this.teleportToBase(player);
		}
	}
	
	// RESET GAME
	public void resetGame()
	{
		initClasses();
		
		// CLEAR PLATES
		clearPlate(RedPlate);
		clearPlate(BluPlate);
		
		// CLEAR INVENTORYS
		clearInventorys(TeamRed);        
		clearInventorys(TeamBlu); 	
		
		// BUILD BASES & PRISON
		buildPlate(RedPlate, EnumTeam.RED);
		buildPlate(BluPlate, EnumTeam.BLU);
		buildPrison();
		
		// TELEPORT TO BASES
		this.teleportTeamToBase(EnumTeam.RED);
		this.teleportTeamToBase(EnumTeam.BLU);
		this.teleportTeamToBase(EnumTeam.REF);
		sendMessageToAll(ChatColor.GOLD + "Game resetted!");
	}
	
	// IS OVER PLATE
	public boolean isOverPlate(HashMap<String, Block> Plate, Block ask)
	{
		for(Block block : Plate.values())
		{
			for(int i = 0; i <= currentSettings.getTowerHeight(); i++)
			{
				if(block.getRelative(0, i, 0).getLocation().equals(ask.getLocation()))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	// IS OVER RED PLATE
	public boolean isOverRedPlate(Block ask)
	{
		return isOverPlate(RedPlate, ask);
	}
	
	// IS OVER BLU PLATE
	public boolean isOverBluPlate(Block ask)
	{
		return isOverPlate(BluPlate, ask);
	}
	
	// BUILD PRISON
	public void buildPrison()
	{
		if(currentSettings.getSpawnPun() == null)
			return;
		
		Location loc = currentSettings.getSpawnPun().getLocation();
		World w = loc.getWorld();
		
		// BODEN
		loc.setY(loc.getY() - 1);
		for(int x = loc.getBlockX() - 4; x <= loc.getBlockX() + 4; x++)
		{
			for(int z = loc.getBlockZ() - 4; z <= loc.getBlockZ() + 4; z++)
			{
				w.getBlockAt(x, loc.getBlockY(), z).setType(Material.BEDROCK);
			}	
		}
		// DECKE
		loc.setY(loc.getY() + 4);
		for(int x = loc.getBlockX() - 4; x <= loc.getBlockX() + 4; x++)
		{
			for(int z = loc.getBlockZ() - 4; z <= loc.getBlockZ() + 4; z++)
			{
				w.getBlockAt(x, loc.getBlockY(), z).setType(Material.BEDROCK);
			}	
		}		
			
		// WAND 1 + 2
		loc.setY(loc.getY() - 4);	
		for(int x = loc.getBlockX() - 4; x <= loc.getBlockX() + 4; x++)
		{
			for(int y = loc.getBlockY(); y <= loc.getBlockY() + 4; y++)
			{
				w.getBlockAt(x, y, loc.getBlockZ() + 4).setType(Material.BEDROCK);
				w.getBlockAt(x, y, loc.getBlockZ() - 4).setType(Material.BEDROCK);
			}
		}	
		// WAND 3 + 4
		for(int z = loc.getBlockZ() - 4; z <= loc.getBlockZ() + 4; z++)
		{
			for(int y = loc.getBlockY(); y <= loc.getBlockY() + 4; y++)		
			{
				w.getBlockAt(loc.getBlockX() - 4, y, z).setType(Material.BEDROCK);
				w.getBlockAt(loc.getBlockX() + 4, y, z).setType(Material.BEDROCK);
			}
		}
		
		// FENSTER 1 + 2
		loc.setY(loc.getY() + 2);
		for(int x = loc.getBlockX() - 2; x <= loc.getBlockX() + 2; x++)
		{
			w.getBlockAt(x, loc.getBlockY(), loc.getBlockZ() + 4).setType(Material.FENCE);
			w.getBlockAt(x, loc.getBlockY(), loc.getBlockZ() - 4).setType(Material.FENCE);
		}
		// FENSTER 3+4
		for(int z = loc.getBlockZ() - 2; z <= loc.getBlockZ() + 2; z++)
		{
			w.getBlockAt(loc.getBlockX() + 4, loc.getBlockY(), z).setType(Material.FENCE);
			w.getBlockAt(loc.getBlockX() - 4, loc.getBlockY(), z).setType(Material.FENCE);
		}
		
		// GLOWSTONE IN DEN ECKEN OBEN
		loc.setY(loc.getY() + 1);
		w.getBlockAt(loc.getBlockX() + 3, loc.getBlockY(), loc.getBlockZ() + 3).setType(Material.GLOWSTONE);
		w.getBlockAt(loc.getBlockX() + 3, loc.getBlockY(), loc.getBlockZ() - 3).setType(Material.GLOWSTONE);
		w.getBlockAt(loc.getBlockX() - 3, loc.getBlockY(), loc.getBlockZ() - 3).setType(Material.GLOWSTONE);
		w.getBlockAt(loc.getBlockX() - 3, loc.getBlockY(), loc.getBlockZ() + 3).setType(Material.GLOWSTONE);
		
		// GLOWSTONE IN DEN ECKEN UNTEN
		loc.setY(loc.getY() - 2);
		w.getBlockAt(loc.getBlockX() + 3, loc.getBlockY(), loc.getBlockZ() + 3).setType(Material.GLOWSTONE);
		w.getBlockAt(loc.getBlockX() + 3, loc.getBlockY(), loc.getBlockZ() - 3).setType(Material.GLOWSTONE);
		w.getBlockAt(loc.getBlockX() - 3, loc.getBlockY(), loc.getBlockZ() - 3).setType(Material.GLOWSTONE);
		w.getBlockAt(loc.getBlockX() - 3, loc.getBlockY(), loc.getBlockZ() + 3).setType(Material.GLOWSTONE);		
	
		// BETT 1 AUFSTELLEN
		w.getBlockAt(loc.getBlockX() + 2, loc.getBlockY(), loc.getBlockZ() - 1).setTypeIdAndData(Material.BED_BLOCK.getId(), (byte)3, false);
		w.getBlockAt(loc.getBlockX() + 3, loc.getBlockY(), loc.getBlockZ() - 1).setTypeIdAndData(Material.BED_BLOCK.getId(), (byte)11, false);
		// BETT 2 AUFSTELLEN
		w.getBlockAt(loc.getBlockX() + 2, loc.getBlockY(), loc.getBlockZ() + 1).setTypeIdAndData(Material.BED_BLOCK.getId(), (byte)3, false);
		w.getBlockAt(loc.getBlockX() + 3, loc.getBlockY(), loc.getBlockZ() + 1).setTypeIdAndData(Material.BED_BLOCK.getId(), (byte)11, false);
		// BETT 3 AUFSTELLEN
		w.getBlockAt(loc.getBlockX() - 2, loc.getBlockY(), loc.getBlockZ() - 1).setTypeIdAndData(Material.BED_BLOCK.getId(), (byte)1, false);
		w.getBlockAt(loc.getBlockX() - 3, loc.getBlockY(), loc.getBlockZ() - 1).setTypeIdAndData(Material.BED_BLOCK.getId(), (byte)9, false);
		// BETT 4 AUFSTELLEN
		w.getBlockAt(loc.getBlockX() - 2, loc.getBlockY(), loc.getBlockZ() + 1).setTypeIdAndData(Material.BED_BLOCK.getId(), (byte)1, false);
		w.getBlockAt(loc.getBlockX() - 3, loc.getBlockY(), loc.getBlockZ() + 1).setTypeIdAndData(Material.BED_BLOCK.getId(), (byte)9, false);
	}
	
	// BUILD PLATE
	public void buildPlate(HashMap<String, Block> Plate, final EnumTeam TeamID)
	{
		for(Block block : Plate.values())
		{
			if(TeamID == EnumTeam.RED)
			{
				block.setTypeIdAndData(Material.WOOL.getId(), (byte)14, true);
			}
			else if(TeamID == EnumTeam.BLU)
			{
				block.setTypeIdAndData(Material.WOOL.getId(), (byte)11, true);
			}
		}
	}
	
	// CLEAR PLATE
	public void clearPlate(HashMap<String, Block> Plate)
	{
		// CLEAR PLATE
		for(Block block : Plate.values())
		{
			for(int i = 1; i <= currentSettings.getTowerHeight(); i++)
			{
				block.getRelative(0, i, 0).setTypeIdAndData(Material.AIR.getId(), (byte)0, true);
			}
		}
	}
	
	// CLEAR INVENTORYS
	public void clearInventorys(HashMap<String, Integer> Team)
	{
		Iterator<String> iterator = Team.keySet().iterator();
		Player player = null;
        while(iterator.hasNext())
        {        
        	player = CoKCore.getPlayer(iterator.next());
        	if(player == null)
        		continue;        
        	
        	player.getInventory().setHelmet(null);
    		player.getInventory().setChestplate(null);
    		player.getInventory().setLeggings(null);
    		player.getInventory().setBoots(null);
    		player.getInventory().clear();        	
		}
	}
	
	// STACK PLATE RED
	public void stackPlateRed(int count)
	{
		stackPlate(RedPlate, count);
	}
	
	// STACK PLATE BLU
	public void stackPlateBlu(int count)
	{
		stackPlate(BluPlate, count);
	}
	
	// LOAD SETTING
	public void loadSettings(CoKGameSettings settings)
	{
		currentSettings = settings;		
		RedPlate = new HashMap<String, Block>();
		BluPlate = new HashMap<String, Block>();		
		for(CoKBlockLocation loc : currentSettings.getRedPlate().values())
		{
			RedPlate.put(loc.getLocation().toString(),loc.getBlock());
		}
		for(CoKBlockLocation loc : currentSettings.getBluPlate().values())
		{
			BluPlate.put(loc.getLocation().toString(),loc.getBlock());
		}
	}
	
	// STACK PLATES
	public void stackPlate(EnumTeam TeamID, int count)
	{		
		if(TeamID == EnumTeam.RED)
		{
			stackPlate(RedPlate, count);
		}
		else if(TeamID == EnumTeam.BLU)
		{
			stackPlate(BluPlate, count);
		}
	}
	
	// STACK PLATES
	public void stackPlate(HashMap<String, Block> Plate, int count)
	{
		// STACK PLATE
		while(count > 0 && isGameOn())
		{			
			for(Block block : Plate.values())
			{
				boolean stacked = false;
				for(int i = 1; i <= currentSettings.getTowerHeight(); i++)
				{
					if(stacked)
						continue;
					
					if(block.getRelative(0, i, 0).getTypeId() != currentSettings.getTowerID() || block.getRelative(0, i, 0).getData() != currentSettings.getTowerData())
					{
						block.getRelative(0, i, 0).setTypeIdAndData(currentSettings.getTowerID(), currentSettings.getTowerData(), true);
						checkGameState();
						count--;
						stacked = true;
						if(count == 0 || !isGameOn())
						{
							return;
						}
					}
				}
			}
		}
		checkGameState();
	}
	
	// SET GAMESTATUS
	public void setGameOn(boolean gameOn)
	{
		this.gameOn = gameOn;
	}
	
	// GET GAMESTATUS
	public boolean isGameOn()
	{
		return this.gameOn;
	}
	
	//////////////////////////////
	//
	// PLAYERCLASS METHODS
	//
	//////////////////////////////	
	
	// INIT CLASSES
	public void initClasses()
	{
		initLeader();
		initArcher();
		initKnight();
		ClassPlayers = new HashMap<EnumTeam, HashMap<String, String>>();
		ClassPlayers.put(EnumTeam.RED, new HashMap<String, String>());
		ClassPlayers.get(EnumTeam.RED).put(LeaderClass.getClassName(), "");
		ClassPlayers.put(EnumTeam.BLU, new HashMap<String, String>());
		ClassPlayers.get(EnumTeam.BLU).put(LeaderClass.getClassName(), "");
		ClassPlayers.get(EnumTeam.RED).put(ArcherClass.getClassName(), "");
		ClassPlayers.get(EnumTeam.BLU).put(ArcherClass.getClassName(), "");
		ClassPlayers.get(EnumTeam.RED).put(KnightClass.getClassName(), "");
		ClassPlayers.get(EnumTeam.BLU).put(KnightClass.getClassName(), "");
	}
	
	// GET TEAMSTRING
	public String getTeamString(final EnumTeam TeamID)
	{
		if(TeamID == EnumTeam.RED)
			return ChatColor.RED + "Team Red" + ChatColor.GOLD;
		else if(TeamID == EnumTeam.BLU)
			return ChatColor.BLUE + "Team Blu" + ChatColor.GOLD;
		else if(TeamID == EnumTeam.BLU)
			return ChatColor.DARK_GRAY + "Team Ref" + ChatColor.GOLD;
		else if(TeamID == EnumTeam.BLU)
			return ChatColor.WHITE + "Team None" + ChatColor.GOLD;
		return "";
	}
	
	// GET NEW CLASSPLAYER STRING
	public String getNewClassPlayerString(CoKClass playerClass, final EnumTeam TeamID)
	{
		//if(playerClass.isShowMSG())
			return ChatColor.GOLD + "New " + playerClass.getClassName() + " of " + getTeamString(TeamID) + ": ";
		//return "";
	}
	
	// GET KILL CLASSPLAYER STRING
	public String getKillClassPlayerString(CoKClass playerClass, final EnumTeam TeamID)
	{
		//if(playerClass.isShowMSG())
			return ChatColor.GOLD + "The " + playerClass.getClassName() + " of " + getTeamString(TeamID) + " got killed!";
		//return "";
	}
	
	//////////////////////////////
	//
	// PLAYERCLASS METHODS
	//
	//////////////////////////////	
	
	// INIT PLAYERCLASS
	public void initPlayerClass(CoKClass playerClass, CoKClassSettings classSettings)
	{
		playerClass.setHelmId(classSettings.getHelmetId());
		playerClass.setChestPlateId(classSettings.getChestPlateId());
		playerClass.setLeggingsId(classSettings.getLeggingsId());
		playerClass.setBootsId(classSettings.getBootsId());
		for(CoKItemStack item : classSettings.getItemList())
		{
			playerClass.addItem(item.getItem());
		}
	}	
	
	//////////////////////////////
	//
	// LEADER METHODS
	//
	//////////////////////////////

	// INIT LEADERCLASS
	public void initLeader()
	{
		LeaderClass = new CoKClassLeader("Leader");		
		initPlayerClass(LeaderClass, currentSettings.getLeaderSettings());
	}
	
	// IS LEADER
	public boolean isLeader(Player player)
	{
		if(!isGameOn())
			return false;

		EnumTeam thisTeam = getTeam(player);
		if(thisTeam != EnumTeam.RED && thisTeam != EnumTeam.BLU)
			return false;
		
		return ClassPlayers.get(thisTeam).get(LeaderClass.getClassName()).equalsIgnoreCase(player.getName());
	}
	
	// GET LEADER
	public String getLeader(final EnumTeam TeamID)
	{
		if(TeamID != EnumTeam.RED && TeamID != EnumTeam.BLU)
			return null;
		
		return ClassPlayers.get(TeamID).get(LeaderClass.getClassName());
	}
			
	// CALL NEW LEADER
	public void callNewLeader(final EnumTeam TeamID)
	{
		if(!currentSettings.isLeaderAllowed())
			return;
		
		if(TeamID == EnumTeam.RED || TeamID == EnumTeam.BLU)
		{
			Random rGen = new Random();
			int rand = rGen.nextInt(getTeamSize(TeamID));
			String newName = getTeamArray(TeamID)[rand];
			if(getLeader(TeamID).equalsIgnoreCase(newName) || getArcher(TeamID).equalsIgnoreCase(newName) || getKnight(TeamID).equalsIgnoreCase(newName))
			{
				this.callNewLeader(TeamID);
				return;
			}
			ClassPlayers.get(TeamID).put(LeaderClass.getClassName(), newName);
			sendMessageToAll(this.getNewClassPlayerString(LeaderClass, TeamID) + getLeader(TeamID));
			LeaderClass.giveKit(CoKCore.getPlayer(getLeader(TeamID)));
		}		
		else
			return;		
	}
		
	//////////////////////////////
	//
	// ARCHER METHODS
	//
	//////////////////////////////
		
	// INIT ARCHERCLASS
	public void initArcher()
	{
		ArcherClass = new CoKClassArcher("Archer");
		initPlayerClass(ArcherClass, currentSettings.getArcherSettings());
	}
		
	// IS ARCHER
	public boolean isArcher(Player player)
	{
		if(!isGameOn())
			return false;

		EnumTeam thisTeam = getTeam(player);
		if(thisTeam != EnumTeam.RED && thisTeam != EnumTeam.BLU)
			return false;
		
		return ClassPlayers.get(thisTeam).get(ArcherClass.getClassName()).equalsIgnoreCase(player.getName());
	}
	
	// GET ARCHER
	public String getArcher(final EnumTeam TeamID)
	{
		if(TeamID != EnumTeam.RED && TeamID != EnumTeam.BLU)
			return null;
		
		return ClassPlayers.get(TeamID).get(ArcherClass.getClassName());
	}
	
	// CALL NEW ARCHER
	public void callNewArcher(final EnumTeam TeamID)
	{
		if(!currentSettings.isArcherAllowed())
			return;
		
		if(TeamID == EnumTeam.RED || TeamID == EnumTeam.BLU)
		{
			Random rGen = new Random();
			int rand = rGen.nextInt(getTeamSize(TeamID));
			String newName = getTeamArray(TeamID)[rand];
			if(getLeader(TeamID).equalsIgnoreCase(newName) || getArcher(TeamID).equalsIgnoreCase(newName) || getKnight(TeamID).equalsIgnoreCase(newName))
			{
				this.callNewArcher(TeamID);
				return;
			}
			ClassPlayers.get(TeamID).put(ArcherClass.getClassName(), newName);
			sendMessageToTeam(TeamID, this.getNewClassPlayerString(ArcherClass, TeamID) + getArcher(TeamID));
			sendMessageToTeam(EnumTeam.REF, this.getNewClassPlayerString(ArcherClass, TeamID) + getArcher(TeamID));
			ArcherClass.giveKit(CoKCore.getPlayer(getArcher(TeamID)));
		}		
		else
			return;
	}
	
	//////////////////////////////
	//
	// KNIGHT METHODS
	//
	//////////////////////////////
	
	// INIT KNIGHTCLASS
	public void initKnight()
	{
		KnightClass = new CoKClassKnight("Knight");
		initPlayerClass(KnightClass, currentSettings.getKnightSettings());
	}
		
	// IS KNIGHT
	public boolean isKnight(Player player)
	{
		if(!isGameOn())
			return false;

		EnumTeam thisTeam = getTeam(player);
		if(thisTeam != EnumTeam.RED && thisTeam != EnumTeam.BLU)
			return false;
		
		return ClassPlayers.get(thisTeam).get(KnightClass.getClassName()).equalsIgnoreCase(player.getName());
	}
	
	// GET KNIGHT
	public String getKnight(final EnumTeam TeamID)
	{
		if(TeamID != EnumTeam.RED && TeamID != EnumTeam.BLU)
			return null;
		
		return ClassPlayers.get(TeamID).get(KnightClass.getClassName());
	}
	
	// CALL NEW KNIGHT
	public void callNewKnight(final EnumTeam TeamID)
	{
		if(!currentSettings.isKnightAllowed())
			return;
		
		if(TeamID == EnumTeam.RED || TeamID == EnumTeam.BLU)
		{
			Random rGen = new Random();
			int rand = rGen.nextInt(getTeamSize(TeamID));
			String newName = getTeamArray(TeamID)[rand];
			if(getLeader(TeamID).equalsIgnoreCase(newName) || getArcher(TeamID).equalsIgnoreCase(newName) || getKnight(TeamID).equalsIgnoreCase(newName))
			{
				this.callNewKnight(TeamID);
				return;
			}
			ClassPlayers.get(TeamID).put(KnightClass.getClassName(), newName);
			sendMessageToTeam(TeamID, this.getNewClassPlayerString(KnightClass, TeamID) + getKnight(TeamID));
			sendMessageToTeam(EnumTeam.REF, this.getNewClassPlayerString(KnightClass, TeamID) + getKnight(TeamID));
			KnightClass.giveKit(CoKCore.getPlayer(getKnight(TeamID)));
		}		
		else
			return;
	}	
	
	//////////////////////////////
	//
	// PLAYER METHODS
	//
	//////////////////////////////
	
	// TELEPORT TO BASE
	public void teleportToBase(Player player)
	{
		// TO RED BASE
		if(isInTeam(player, EnumTeam.RED) && currentSettings.getSpawnRed() != null)
		{
			player.teleport(currentSettings.getSpawnRed().getLocation());
			return;
		}
		// TO BLU BASE
		else if(isInTeam(player, EnumTeam.BLU) && currentSettings.getSpawnBlu() != null)
		{
			player.teleport(currentSettings.getSpawnBlu().getLocation());
			return;
		}
		// TO REF BASE
		else if(isInTeam(player, EnumTeam.REF) && currentSettings.getSpawnRef() != null)
		{
			player.teleport(currentSettings.getSpawnRef().getLocation());
			return;
		}
	}
	
	// SEND MESSAGE TO ALL
	public void sendMessageToAll(String message)
	{
		sendMessageToTeam(PlayerList, message);
	}
	
	// SEND MESSAGE TO TEAM
	public void sendMessageToTeam(HashMap<String, Integer> Team, String message)
	{
		// CLEAR INVENTORY: RED TEAM
		Iterator<String> iterator = Team.keySet().iterator();
		Player player = null;
        while(iterator.hasNext())
        {        
        	player = CoKCore.getPlayer(iterator.next());
        	if(player == null)
        		continue;        	
        	player.sendMessage(message);
		}
	}
	
	// SEND MESSAGE TO TEAM
	public void sendMessageToTeam(EnumTeam TeamID, String message)
	{
		if(TeamID == EnumTeam.RED)
			sendMessageToTeam(TeamRed, message);
		else if(TeamID == EnumTeam.BLU)
			sendMessageToTeam(TeamBlu, message);
		else if(TeamID == EnumTeam.REF)
			sendMessageToTeam(TeamRef, message);
		else
			sendMessageToAll(message);
	}	
	
	// SEND MESSAGE
	public void sendMessage(Player player, String message)
	{
		player.sendMessage(message);
	}
	
	// IS PLAYER IN GAME
	public boolean isInGame(Player player)
	{
		return PlayerList.containsKey(player.getName());
	}
	
	// JOIN GAME
	public boolean joinGame(Player player)
	{
		if(isInGame(player))
			return false;
		
		PlayerList.put(player.getName(), 0);
		sendMessageToAll(ChatColor.GOLD + player.getName() + " joined the game!");
		
		if(TeamRed.size() <= TeamBlu.size())
		{
			joinTeam(player, EnumTeam.RED);
		}
		else if(TeamRed.size() > TeamBlu.size())
		{
			joinTeam(player, EnumTeam.BLU);
		}		
		return true;
	}
	
	// MOVE PLAYER TO TEAM
	public boolean movePlayer(Player player, final EnumTeam TeamID)
	{
		if(isInGame(player))
			return false;
		
		PlayerList.put(player.getName(), 0);
		joinTeam(player, TeamID);		
		sendMessageToAll(ChatColor.GOLD + player.getName() + " was moved to " + this.getTeamString(TeamID) + " by a referee!");	
		return true;
	}
	
	// LEAVE GAME
	public boolean leaveGame(Player player)
	{
		if(!isInGame(player))
			return false;
		
		if(this.isGameOn())
		{		
			EnumTeam thisTeam = getTeam(player);
			if(thisTeam == EnumTeam.RED || thisTeam == EnumTeam.BLU)
			{
				if(getLeader(thisTeam) != null)
				{
					if(isLeader(player))
					{
						callNewLeader(thisTeam);
						LeaderClass.clearKit(player);
					}
				}		
				else if(getArcher(thisTeam) != null)
				{
					if(isArcher(player))
					{
						callNewArcher(thisTeam);
						ArcherClass.clearKit(player);
					}
				}
				else if(getKnight(thisTeam) != null)
				{
					if(isKnight(player))
					{
						callNewKnight(thisTeam);
						KnightClass.clearKit(player);
					}
				}
			}			
		}
		
		leaveAllTeams(player);
		sendMessage(player, ChatColor.GOLD + "You left the game!");
		PlayerList.remove(player.getName());
		sendMessageToAll(ChatColor.GOLD + player.getName() + " left the game!");
		return true;
	}
	
	// CLOSE GAME
	public void closeGame()
	{
		sendMessageToAll(ChatColor.AQUA + "Game was closed by the gameowner!");
		PlayerList.clear();
		TeamRed.clear();
		TeamBlu.clear();
		TeamRef.clear();
		RedPlate.clear();
		BluPlate.clear();
		currentSettings = null;	
		CoKCore.GameList.remove(gameName);
	}
	
	// GET TEAM
	public EnumTeam getTeam(Player player)
	{
		if(TeamRed.containsKey(player.getName()))
			return EnumTeam.RED;
		else if(TeamBlu.containsKey(player.getName()))
			return EnumTeam.BLU;
		if(TeamRef.containsKey(player.getName()))
			return EnumTeam.REF;
		return EnumTeam.NONE;
	}
	
	// GET TEAMSIZE
	public int getTeamSize(final EnumTeam TeamID)
	{
		if(TeamID == EnumTeam.RED)
			return TeamRed.size();
		else if(TeamID == EnumTeam.BLU)
			return TeamBlu.size();
		else if(TeamID == EnumTeam.REF)
			return TeamRef.size();		
		return 0;
	}
	
	// GET PLATESIZE
	public int getPlateSize(final EnumTeam TeamID)
	{
		if(TeamID == EnumTeam.RED)
			return RedPlate.values().size();
		else if(TeamID == EnumTeam.BLU)
			return BluPlate.values().size();
		return 0;
	}	

	// GET TEAMARRAY
	public String[] getTeamArray(final EnumTeam TeamID)
	{
		if(TeamID == EnumTeam.RED)
		{
			String[] arr = new String[TeamRed.keySet().size()];
			int i = 0;
			for(String str : TeamRed.keySet())
			{
				arr[i] = str;
				i++;
			}
			return arr;
		}
		else if(TeamID == EnumTeam.BLU)
		{
			String[] arr = new String[TeamBlu.keySet().size()];
			int i = 0;
			for(String str : TeamBlu.keySet())
			{
				arr[i] = str;
				i++;
			}
			return arr;
		}
		else if(TeamID == EnumTeam.REF)
		{
			String[] arr = new String[TeamRef.keySet().size()];
			int i = 0;
			for(String str : TeamRef.keySet())
			{
				arr[i] = str;
				i++;
			}
			return arr;
		}
		return null;
	}
	
	// JOIN TEAM
	public boolean joinTeam(Player player, final EnumTeam TeamID)
	{
		if(!isInGame(player))
			return false;
		
		// ALREADY IN THAT TEAM?
		if(isInTeam(player, TeamID))
			return false;
		
		// LEAVE TEAM
		leaveAllTeams(player);
		
		// JOIN NEW TEAM
		switch(TeamID)
		{
			case NONE:
			{
				// NONE
				sendMessageToAll(ChatColor.GOLD + player.getName() + " joined Team: " + ChatColor.WHITE + "NONE");
				teleportToBase(player);
				break;
			}
			case RED:
			{
				// RED
				TeamRed.put(player.getName(), 0);
				sendMessageToAll(ChatColor.GOLD + player.getName() + " joined Team: " + ChatColor.RED + "RED");
				teleportToBase(player);
				break;
			}
			case BLU:
			{
				// BLU
				TeamBlu.put(player.getName(), 0);
				sendMessageToAll(ChatColor.GOLD + player.getName() + " joined Team: " + ChatColor.BLUE + "BLU");
				teleportToBase(player);
				break;
			}
			case REF:
			{
				// REFEREE
				TeamRef.put(player.getName(), 0);
				sendMessageToAll(ChatColor.GOLD + player.getName() + " joined Team: " + ChatColor.DARK_GRAY + "REFEREE");
				teleportToBase(player);
				break;
			}
			default:
			{
				// FEHLER
				return false;
			}
		}		
		return true;
	}
	
	// IS IN TEAM
	public boolean isInTeam(Player player, final EnumTeam TeamID)
	{
		if(!isInGame(player))
			return false;
				
		switch(TeamID)
		{
			case NONE:
			{
				// NONE
				return false;
			}
			case RED:
			{
				// RED
				return TeamRed.containsKey(player.getName());
			}
			case BLU:
			{
				// BLU
				return TeamBlu.containsKey(player.getName());
			}
			case REF:
			{
				// REFEREE
				return TeamRef.containsKey(player.getName());
			}
			default:
			{
				// NOT IN GAME
				return false;
			}
		}
	}
	
	// LEAVE ALL TEAM
	public boolean leaveAllTeams(Player player)
	{
		if(!isInGame(player))
			return false;
		
		leaveTeam(player, EnumTeam.NONE);
		leaveTeam(player, EnumTeam.RED);
		leaveTeam(player, EnumTeam.BLU);
		leaveTeam(player, EnumTeam.REF);
		return true;
	}
	
	// CHECK TEAMS AND LEADER
	public boolean checkTeamsAndLeader(Player player)
	{
		if(this.isGameOn())
		{			
			if(TeamRed.size() < getMinPlayerCount() || TeamBlu.size() < getMinPlayerCount())
			{
				if(!isGamePaused())
					setGamePaused(true);
				return true;
			}
			EnumTeam thisTeam = getTeam(player);
			if(thisTeam == EnumTeam.RED || thisTeam == EnumTeam.BLU)
			{
				if(getLeader(thisTeam) != null)
				{
					if(isLeader(player))
					{
						callNewLeader(thisTeam);
						LeaderClass.clearKit(player);
					}
				}
				else if(getArcher(thisTeam) != null)
				{
					if(isArcher(player))
					{
						callNewArcher(thisTeam);
						ArcherClass.clearKit(player);
					}
				}
				else if(getKnight(thisTeam) != null)
				{
					if(isKnight(player))
					{
						callNewKnight(thisTeam);
						KnightClass.clearKit(player);
					}
				}
			}
		}
		return true;
	}
	
	// LEAVE TEAM
	public boolean leaveTeam(Player player, final EnumTeam TeamID)
	{
		if(!isInGame(player))
			return false;
		
		if(!isInTeam(player, TeamID))
			return false;
				
		switch(TeamID)
		{
			case NONE:
			{
				// NONE
				return true;
			}
			case RED:
			{
				// RED
				TeamRed.remove(player.getName());
				sendMessageToAll(ChatColor.GOLD + player.getName() + " left Team: " + ChatColor.RED + "RED");
				checkTeamsAndLeader(player);
				return true;
			}
			case BLU:
			{
				// BLU
				TeamBlu.remove(player.getName());
				sendMessageToAll(ChatColor.GOLD + player.getName() + " left Team: " + ChatColor.BLUE + "BLU");
				checkTeamsAndLeader(player);
				return true;
			}
			case REF:
			{
				// REFEREE
				TeamRef.remove(player.getName());
				sendMessageToAll(ChatColor.GOLD + player.getName() + " left Team: " + ChatColor.DARK_GRAY + "REFEREE");
				checkTeamsAndLeader(player);
				return true;
			}
			default:
			{
				return false;
			}
		}	
	}

	// PUNISH PLAYER
	public void punishPlayer(Player player)
	{
		TeamPun.put(player.getName(), 0);
		player.teleport(currentSettings.getSpawnPun().getLocation());
		sendMessageToAll("'" + player.getName() + "' gets punished by a referee!");
	}
	
	// DEPUNISH PLAYER
	public void depunishPlayer(Player player)
	{
		TeamPun.remove(player.getName());
		teleportToBase(player);
		sendMessageToAll("'" + player.getName() + "' gets depunished and can play again!");
	}

	// PUNISH TEAM
	public void punishTeam(String team, int count)
	{
		if(team.equalsIgnoreCase("red"))
		{
			stackPlateRed(count);
			sendMessageToAll(getTeamString(EnumTeam.RED) + " gets punished by " + count + " blocks!");
		}
		else if(team.equalsIgnoreCase("blu"))
		{
			stackPlateBlu(count);
			sendMessageToAll(getTeamString(EnumTeam.BLU) + " gets punished by " + count + " blocks!");
		}
	}

	/**
	 * @return the leaderClass
	 */
	public CoKClass getLeaderClass() {
		return LeaderClass;
	}

	/**
	 * @return the archerClass
	 */
	public CoKClass getArcherClass() {
		return ArcherClass;
	}
	
	/**
	 * @return the knightClass
	 */
	public CoKClass getKnightClass() {
		return KnightClass;
	}
}