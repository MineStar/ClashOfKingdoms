package com.bukkit.gemo.ClashOfKingdoms.Listener;

import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import com.bukkit.gemo.ClashOfKingdoms.*;
import com.bukkit.gemo.ClashOfKingdoms.game.*;

public class CoKBL extends BlockListener
{
	public static CoKCore plugin;	
	
	////////////////////////////////
	//
	// CONSTRUCTOR
	//
	////////////////////////////////
	public CoKBL(CoKCore instance)
	{
    	plugin = instance; 
	}

	////////////////////////////////
	//
	// ON BLOCK PLACE
	//
	////////////////////////////////
	@Override
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Player player = (Player)event.getPlayer();		
		if(!CoKCore.isPlayerInAnyGame(player))
		{
			Iterator<CoKGame> iterator = CoKCore.GameList.values().iterator();
			CoKGame game = null;
			Block block = event.getBlock();
			while(iterator.hasNext())
			{			
				game = iterator.next();
				if(game.isGameOn() && (game.isOverRedPlate(block) || game.isOverBluPlate(block)))
				{
					event.setCancelled(true);
					break;
				}
			}
			game = null;
			return;	
		}
		
		CoKGame game = CoKCore.getGameByPlayer(player);
		if(!game.isGameOn())		
			return;
		
		if(game.isGamePaused())
		{
			event.setBuild(false);
			event.setCancelled(true);
			return;
		}
		
		Block block = event.getBlock();		
		if(game.isOverRedPlate(block))
		{			
			if(!game.isInTeam(player, EnumTeam.BLU) || game.isArcher(player))
			{
				event.setBuild(false);
				event.setCancelled(true);
				return;
			}
			else
			{
				if(block.getTypeId() != game.currentSettings.getTowerID() || block.getData() != game.currentSettings.getTowerData())
				{
					event.setBuild(false);
					event.setCancelled(true);
					return;
				}	
			}
		}
		else if(game.isOverBluPlate(block))
		{		
			if(!game.isInTeam(player, EnumTeam.RED)  || game.isArcher(player))
			{
				event.setBuild(false);
				event.setCancelled(true);
				return;
			}
			else
			{
				if(block.getTypeId() != game.currentSettings.getTowerID() || block.getData() != game.currentSettings.getTowerData())
				{
					event.setBuild(false);
					event.setCancelled(true);
					return;
				}	
			}
		}
		
		if(game.isOverRedPlate(block) && game.isInTeam(player, EnumTeam.BLU))
		{
			game.sendMessageToAll(ChatColor.WHITE + player.getName() + ChatColor.GOLD + " placed a block at " + game.getTeamString(EnumTeam.RED) );
		}
		else if(game.isOverBluPlate(block) && game.isInTeam(player, EnumTeam.RED))
		{
			game.sendMessageToAll(ChatColor.WHITE + player.getName() + ChatColor.GOLD + " placed a block at " + game.getTeamString(EnumTeam.BLU) );
		}
		
		// CHECK IF TOWER IS COMPLETE
		game.checkGameState();
	}

	////////////////////////////////
	//
	// ON BLOCK BREAK
	//
	////////////////////////////////
	@Override
	public void onBlockBreak(BlockBreakEvent event)
	{
		Player player = (Player)event.getPlayer();		
		if(!CoKCore.isPlayerInAnyGame(player))
		{
			Iterator<CoKGame> iterator = CoKCore.GameList.values().iterator();
			CoKGame game = null;
			Block block = event.getBlock();
			while(iterator.hasNext())
			{			
				game = iterator.next();
				if(game.isGameOn() && (game.isOverRedPlate(block) || game.isOverBluPlate(block)))
				{
					event.setCancelled(true);
					break;
				}
			}
			game = null;
			return;	
		}
		
		CoKGame game = CoKCore.getGameByPlayer(player);
		if(!game.isGameOn())		
			return;
		
		if(game.isGamePaused())
		{
			event.setCancelled(true);
			return;
		}
		
		Block block = event.getBlock();
		if(game.isOverRedPlate(block) || game.isOverBluPlate(block))
		{
			if(!game.isInTeam(player, EnumTeam.REF))
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	////////////////////////////////
	//
	// ON BLOCK IGNITE
	//
	////////////////////////////////
	@Override
	public void onBlockIgnite(BlockIgniteEvent event)
	{
		Iterator<CoKGame> iterator = CoKCore.GameList.values().iterator();
		CoKGame game = null;
		Block block = event.getBlock();
		while(iterator.hasNext())
		{			
			game = iterator.next();
			if(game.isGameOn() && (game.isOverRedPlate(block) || game.isOverBluPlate(block)))
			{
				event.setCancelled(true);
				break;
			}
		}
		game = null;
		return;		
	}

	////////////////////////////////
	//
	// ON BLOCK BURN
	//
	////////////////////////////////
	@Override
	public void onBlockBurn(BlockBurnEvent event)
	{
		if(!event.getBlock().getWorld().getName().equalsIgnoreCase("cok"))
			return;
		
		event.setCancelled(true);
	}

	////////////////////////////////
	//
	// ON BLOCK FORM
	//
	////////////////////////////////
	@Override
	public void onBlockForm(BlockFormEvent event)
	{
		if(!event.getBlock().getWorld().getName().equalsIgnoreCase("cok"))
			return;
		
		event.setCancelled(true);
	}

	////////////////////////////////
	//
	// ON BLOCK SPREAD
	//
	////////////////////////////////
	@Override
	public void onBlockSpread(BlockSpreadEvent event) 
	{
		if(!event.getBlock().getWorld().getName().equalsIgnoreCase("cok"))
			return;
		
		event.setCancelled(true);
	}


	////////////////////////////////
	//
	// ON LEAVES DECAY
	//
	////////////////////////////////
	@Override
	public void onLeavesDecay(LeavesDecayEvent event) 
	{
		if(!event.getBlock().getWorld().getName().equalsIgnoreCase("cok"))
			return;
		
		event.setCancelled(true);
	}

	////////////////////////////////
	//
	// ON BLOCK FADE
	//
	////////////////////////////////
	@Override
	public void onBlockFade(BlockFadeEvent event)
	{
		if(!event.getBlock().getWorld().getName().equalsIgnoreCase("cok"))
			return;
		
		event.setCancelled(true);
	}

}
