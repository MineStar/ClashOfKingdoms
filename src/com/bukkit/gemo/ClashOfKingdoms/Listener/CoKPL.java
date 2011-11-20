package com.bukkit.gemo.ClashOfKingdoms.Listener;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import com.bukkit.gemo.ClashOfKingdoms.*;
import com.bukkit.gemo.ClashOfKingdoms.game.*;

public class CoKPL extends PlayerListener
{
	public static CoKCore plugin;		
	public static HashMap<String, CoKSelectArea> Selections = new HashMap<String, CoKSelectArea>();

	////////////////////////////////
	//
	// CONSTRUCTOR
	//
	////////////////////////////////
	public CoKPL(CoKCore instance)
	{
    	plugin = instance; 
	}
	
	////////////////////////////////
	//
	// ON PLAYER INTERACT ENTITY
	//
	////////////////////////////////
	@Override
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) 
	{
		Player player = (Player)event.getPlayer();		
		if(!CoKCore.isPlayerInAnyGame(player))
			return;	
		
		CoKGame game = CoKCore.getGameByPlayer(player);
		if(game.isGameOn() && game.isGamePaused())
		{
			event.setCancelled(true);
		}
	}
	
	////////////////////////////////
	//
	// ON PLAYER CHAT
	//
	////////////////////////////////
	@Override
	public void onPlayerChat(PlayerChatEvent event)
	{
		if(event.isCancelled())
			return;
		
		Player player =(Player)event.getPlayer();
		
		if(!CoKCore.isPlayerInAnyGame(player))
		{
			Iterator<Player> iP = event.getRecipients().iterator();
			while(iP.hasNext())
			{
			    Player nPlayer = iP.next();
			    if(CoKCore.isPlayerInAnyGame(nPlayer))
			    	if(CoKCore.getGameByPlayer(nPlayer).gameOn && !CoKCore.getGameByPlayer(nPlayer).isGamePaused())
			    		iP.remove();			    
			}
			return;	
		}		
		
		CoKGame game = CoKCore.getGameByPlayer(player);
		if(game.isInTeam(player, EnumTeam.REF) && game.isGameOn() && !game.isGamePaused())
		{
			game.sendMessageToAll(ChatColor.DARK_GRAY + player.getName() + ": " + ChatColor.WHITE + event.getMessage());
			event.setMessage("");
			event.setCancelled(true);
		}
		else if(game.isInTeam(player, EnumTeam.RED) && game.isGameOn() && !game.isGamePaused())
		{
			game.sendMessageToTeam(game.TeamRed, ChatColor.DARK_RED + player.getName() + ": " + ChatColor.WHITE + event.getMessage());
			game.sendMessageToTeam(game.TeamRef, ChatColor.DARK_RED + player.getName() + ": " + ChatColor.WHITE + event.getMessage());
			event.setMessage("");
			event.setCancelled(true);
		}
		else if(game.isInTeam(player, EnumTeam.BLU) && game.isGameOn() && !game.isGamePaused())
		{
			game.sendMessageToTeam(game.TeamBlu, ChatColor.DARK_BLUE + player.getName() + ": " + ChatColor.WHITE + event.getMessage());
			game.sendMessageToTeam(game.TeamRef, ChatColor.DARK_BLUE + player.getName() + ": " + ChatColor.WHITE + event.getMessage());
			event.setMessage("");
			event.setCancelled(true);
		}
	}

	////////////////////////////////
	//
	// ON PLAYER QUIT
	//
	////////////////////////////////
	@Override
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Player player = (Player)event.getPlayer();		
		if(!CoKCore.isPlayerInAnyGame(player))
			return;	
		
		CoKGame game = CoKCore.getGameByPlayer(player);
		
		// AUS TEAMS LÖSCHEN
		game.leaveGame(player);
	}

	////////////////////////////////
	//
	// ON PLAYER INTERACT
	//
	////////////////////////////////
	@Override
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(event.getAction() != Action.LEFT_CLICK_BLOCK)
			return;
		
		Player player = (Player)event.getPlayer();
		
		if(!CoKCore.isPlayerInAnyGame(player))
			return;
		
		CoKGame currentGame = CoKCore.getGameByPlayer(player);
		
		// HANLDE REFEREES
		if(!currentGame.isGameOn())
		{
			if(!currentGame.isInTeam(player, EnumTeam.REF))
				return;
			
			Block block = event.getClickedBlock();
			if(player.getItemInHand().getType().equals(Material.BEDROCK))
			{
				// TEAM 1 = ROTE WOLLE ALS BASIS
				if(block.getType().equals(Material.WOOL) && block.getData() == 14)
				{
					if(!currentGame.RedPlate.containsKey(block.getLocation().toString()))
					{
						currentGame.RedPlate.put(block.getLocation().toString(), block);
						player.sendMessage(ChatColor.GREEN + "Added block: Team Red");
					}
					else
					{
						currentGame.RedPlate.remove(block.getLocation().toString());
						player.sendMessage(ChatColor.RED + "Removed block: Team Red");
					}
				}
				// TEAM 2 = BLAUE WOLLE ALS BASIS
				if(block.getType().equals(Material.WOOL) && block.getData() == 11)
				{
					if(!currentGame.BluPlate.containsKey(block.getLocation().toString()))
					{
						currentGame.BluPlate.put(block.getLocation().toString(), block);
						player.sendMessage(ChatColor.GREEN + "Added block: Team Blu");
					}
					else
					{
						currentGame.BluPlate.remove(block.getLocation().toString());
						player.sendMessage(ChatColor.RED + "Removed block: Team Blu");
					}
				}	
			}
			else if(player.getItemInHand().getType().equals(Material.DIAMOND_HOE))
			{
				event.setUseItemInHand(Event.Result.DENY);
				event.setUseInteractedBlock(Event.Result.DENY);
				event.setCancelled(true);
				Location loc 	= event.getClickedBlock().getLocation();
				String worldName = event.getClickedBlock().getLocation().getWorld().getName();
				
				CoKSelectArea select = Selections.get(player.getName());
				if(select == null)
				{
					// CREATE NEW SELECTION
					select = new CoKSelectArea(worldName);
					select.add(loc);
					player.sendMessage(ChatColor.LIGHT_PURPLE + "[CoK] Position " + select.getSize() + " selected");				
					Selections.put(player.getName(), select);
				}
				else
				{
					// ADD POINT TO SELECTION
					if(select.getWorldName().equalsIgnoreCase(worldName) && select.getSize() < 2)
					{
						select.add(loc);
						player.sendMessage(ChatColor.LIGHT_PURPLE + "[CoK] Position " + select.getSize() + " selected");					
					}
					else
					{
						// CREATE NEW SELECTION
						CoKSelectArea newSelect = new CoKSelectArea(worldName);
						newSelect.add(loc);
						Selections.remove(player.getName());
						Selections.put(player.getName(), newSelect);
						player.sendMessage(ChatColor.LIGHT_PURPLE + "[CoK] Position " + newSelect.getSize() + " selected");				
					}
				}		
			}
			currentGame.currentSettings.setData(currentGame.RedPlate, currentGame.BluPlate);
		}
		else
		{
			// HANDLE PLAYERS
			if(currentGame.isGameOn() && currentGame.isGamePaused())
			{
				event.setUseItemInHand(Event.Result.DENY);
				event.setUseInteractedBlock(Event.Result.DENY);
				event.setCancelled(true);
			}
		}
	}

	////////////////////////////////
	//
	// ON PLAYER RESPAWN
	//
	////////////////////////////////
	@Override
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		Player player = (Player)event.getPlayer();
		
		if(!CoKCore.isPlayerInAnyGame(player))
			return;
		
		CoKGame currentGame = CoKCore.getGameByPlayer(player);
			
		// CALL NEW LEADER, ARCHER, KNIGHT
		if(currentGame.gameOn)
		{		
			EnumTeam thisTeam = currentGame.getTeam(player);
			if(thisTeam == EnumTeam.RED || thisTeam == EnumTeam.BLU)
			{			
				if(currentGame.isLeader(player))
				{
					currentGame.callNewLeader(thisTeam);
				}
				else if(currentGame.isArcher(player))
				{
					currentGame.callNewArcher(thisTeam);
				}
				else if(currentGame.isKnight(player))
				{
					currentGame.callNewKnight(thisTeam);
				}
			}
		}
			
		// TELEPORT TO BASE
		if(currentGame.isInTeam(player, EnumTeam.RED))
			event.setRespawnLocation(currentGame.currentSettings.getSpawnRed().getLocation());
		if(currentGame.isInTeam(player, EnumTeam.BLU))
			event.setRespawnLocation(currentGame.currentSettings.getSpawnBlu().getLocation());
		if(currentGame.isInTeam(player, EnumTeam.REF))
			event.setRespawnLocation(currentGame.currentSettings.getSpawnRef().getLocation());		
	}

	////////////////////////////////
	//
	// ON ITEM DROP
	//
	////////////////////////////////
	@Override
	public void onPlayerDropItem(PlayerDropItemEvent event)
	{
		Player player = (Player)event.getPlayer();		
		if(!CoKCore.isPlayerInAnyGame(player))
			return;
		
		CoKGame currentGame = CoKCore.getGameByPlayer(player);
		if(!currentGame.gameOn)
			return;		
		
		if(currentGame.isGamePaused())
		{
			event.setCancelled(true);
			return;
		}
		
		if(currentGame.isInTeam(player, EnumTeam.REF))
		{
			event.setCancelled(true);
			return;
		}
		
		EnumTeam thisTeam = currentGame.getTeam(player);
		if(thisTeam == EnumTeam.RED || thisTeam == EnumTeam.BLU)
		{
			if(currentGame.isLeader(player))
			{
				ItemStack iS = event.getItemDrop().getItemStack();
				if(!currentGame.getLeaderClass().canDrop(iS))
				{
					event.setCancelled(true);
					return;
				}
			}
			else if(currentGame.isArcher(player))
			{
				ItemStack iS = event.getItemDrop().getItemStack();
				if(!currentGame.getArcherClass().canDrop(iS))
				{
					event.setCancelled(true);
					return;
				}
			}
		}				
	}
	
	////////////////////////////////
	//
	// ON ITEM PICKUP
	//
	////////////////////////////////
	@Override
	public void onPlayerPickupItem(PlayerPickupItemEvent event)
	{
		Player player = (Player)event.getPlayer();		
		if(!CoKCore.isPlayerInAnyGame(player))
			return;
		
		CoKGame currentGame = CoKCore.getGameByPlayer(player);		
		if(!currentGame.gameOn)
			return;

		if(currentGame.isGamePaused())
		{
			event.setCancelled(true);
			return;
		}
		
		if(currentGame.isInTeam(player, EnumTeam.REF))
		{
			event.setCancelled(true);
			return;
		}
		
		if(event.getItem().getItemStack().getTypeId() == Material.ARROW.getId() || event.getItem().getItemStack().getTypeId() == Material.BOW.getId())
		{
			if(!currentGame.isArcher(player))
			{
				event.setCancelled(true);
				return;
			}
		}		
	}
}
