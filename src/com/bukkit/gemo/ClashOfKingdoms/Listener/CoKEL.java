package com.bukkit.gemo.ClashOfKingdoms.Listener;

import java.util.Iterator;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;
import com.bukkit.gemo.ClashOfKingdoms.*;
import com.bukkit.gemo.ClashOfKingdoms.game.*;

public class CoKEL extends EntityListener
{
	public static CoKCore plugin;	
	
	////////////////////////////////
	//
	// CONSTRUCTOR
	//
	////////////////////////////////
	public CoKEL(CoKCore instance)
	{
    	plugin = instance; 
	}
	
	////////////////////////////////
	//
	// ON EXPLODE
	//
	////////////////////////////////
	@Override
	public void onEntityExplode(EntityExplodeEvent event)
	{
		List<Block> liste = event.blockList();
		for(Block block : liste)
		{
			Iterator<CoKGame> iterator = CoKCore.GameList.values().iterator();
			CoKGame game = null;
			while(iterator.hasNext())
			{			
				game = iterator.next();
				if(game.isGameOn() && (game.isOverRedPlate(block) || game.isOverBluPlate(block)))
				{
					event.setYield(0f);
					event.blockList().clear();
					event.setCancelled(true);
					return;
				}
			}			
			game = null;
		}
	}
	
	////////////////////////////////
	//
	// ON DEATH
	//
	////////////////////////////////
	@Override
	public void onEntityDeath(EntityDeathEvent event)
	{
		if(!(event.getEntity() instanceof Player))
			return;
		
		Player player = (Player)event.getEntity();
		
		Iterator<CoKGame> iterator = CoKCore.GameList.values().iterator();
		CoKGame game = null;
		while(iterator.hasNext())
		{			
			game = iterator.next();
			if(game.isGameOn() && game.isInGame(player))
			{
				EnumTeam thisTeam = game.getTeam(player);
				if(thisTeam == EnumTeam.RED || thisTeam == EnumTeam.BLU)
				{
					if(game.isLeader(player))
					{
						event.getDrops().clear();
						game.stackPlate(thisTeam, game.getLeaderClass().getPunishment(game.getPlateSize(thisTeam)));
						game.sendMessageToAll(game.getKillClassPlayerString(game.getLeaderClass(), thisTeam));
						return;
					}
					else if(game.isArcher(player))
					{
						event.getDrops().clear();
						game.stackPlate(thisTeam, game.getArcherClass().getPunishment(game.getPlateSize(thisTeam)));
						game.sendMessageToAll(game.getKillClassPlayerString(game.getArcherClass(), thisTeam));
						return;
					}
					else if(game.isKnight(player))
					{
						event.getDrops().clear();
						game.stackPlate(thisTeam, game.getKnightClass().getPunishment(game.getPlateSize(thisTeam)));
						game.sendMessageToAll(game.getKillClassPlayerString(game.getKnightClass(), thisTeam));
						return;
					}
				}
			}
		}
		game = null;
		return;	
	}

	////////////////////////////////
	//
	// ON DAMAGE
	//
	////////////////////////////////
	@Override
	public void onEntityDamage(EntityDamageEvent event)
	{
		if(!(event.getEntity() instanceof Player))
			return;
		
		Player player = (Player)event.getEntity();		
		if(!CoKCore.isPlayerInAnyGame(player))
		{
			return;
		}
		
		CoKGame game = CoKCore.getGameByPlayer(player);
		if(!game.isGameOn())		
			return;
		
		if(game.isGamePaused())
		{
			event.setDamage(0);
			event.setCancelled(true);
		}
		
		if(game.isInTeam(player, EnumTeam.REF))
		{
			event.setDamage(0);
			event.setCancelled(true);
		}
	}
}
