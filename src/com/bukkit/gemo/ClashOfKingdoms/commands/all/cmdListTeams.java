package com.bukkit.gemo.ClashOfKingdoms.commands.all;

import java.util.Iterator;
import com.bukkit.gemo.ClashOfKingdoms.game.*;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.bukkit.gemo.ClashOfKingdoms.*;

public class cmdListTeams
{
	private static String Description = "Show the teams";
	private static String Syntax = "/cok teams";
	private static String[] Examples = {};
	
	// CHECK SYNTAX
	public static boolean checkSyntax(String[] args)
	{
		if(args.length != 1)
		{
			return false;
		}
				
		return true;
	}
		
	// EXECUTE THE COMMAND
	public static void ExecuteCommand(Player player, String[] args)
	{
		// CHECK COMMAND SYNTAX
		if(!checkSyntax(args))
		{
			CoKChatUtils.printWrongSyntax(player, Syntax, Examples);
			return;
		}
		
		// EXECUTE COMMAND
		if(!CoKCore.isPlayerInAnyGame(player))
		{
			CoKChatUtils.printError(player, "You must join a game first!");
			return;
		}
		
		CoKGame game = CoKCore.getGameByPlayer(player);
		
		// TEAM RED
		Iterator<String> iterator = game.TeamRed.keySet().iterator();
		String listRed = "";
		while(iterator.hasNext())
		{		
			listRed += ChatColor.WHITE + iterator.next() + ChatColor.GRAY + ", ";
		}
		
		// TEAM BLU
		iterator = game.TeamBlu.keySet().iterator();
		String listBlu = "";
		while(iterator.hasNext())
		{		
			listBlu += ChatColor.WHITE + iterator.next() + ChatColor.GRAY + ", ";
		}
		
		// TEAM REF
		iterator = game.TeamRef.keySet().iterator();
		String listRef = "";
		while(iterator.hasNext())
		{		
			listRef += ChatColor.WHITE + iterator.next() + ChatColor.GRAY + ", ";
		}
		
		// OUTPUT
		player.sendMessage(ChatColor.AQUA + "[Clash of Kingdoms - Teams]");
		player.sendMessage(ChatColor.RED + "Team Red: " + listRed);		
		player.sendMessage(ChatColor.BLUE + "Team Blu: " + listBlu);;
		player.sendMessage(ChatColor.DARK_GRAY + "Referees: " + listRef);;
		return;		
	}
	
	public static void setDescription(String description) {
		Description = description;
	}

	public static String getDescription() {
		return Description;
	}
	
	public static String getSyntax() {
		return Syntax;
	}

	public static void setSyntax(String syntax) {
		Syntax = syntax;
	}

	public static String[] getExamples() {
		return Examples;
	}

	public static void setExamples(String[] examples) {
		Examples = examples;
	}
}
