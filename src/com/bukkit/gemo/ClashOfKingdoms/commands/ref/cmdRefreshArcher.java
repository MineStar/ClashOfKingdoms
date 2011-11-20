package com.bukkit.gemo.ClashOfKingdoms.commands.ref;

import org.bukkit.entity.Player;
import com.bukkit.gemo.ClashOfKingdoms.game.*;
import com.bukkit.gemo.ClashOfKingdoms.*;

public class cmdRefreshArcher
{
	private static String Description = "Call a new archer";
	private static String Syntax = "/cok newArcher red|blu";
	private static String[] Examples = {"/cok newArcher blu"};
	
	// CHECK SYNTAX
	public static boolean checkSyntax(String[] args)
	{
		if(args.length != 2)
		{
			return false;
		}
		
		try
		{
			if(!args[1].equalsIgnoreCase("red") && !args[1].equalsIgnoreCase("blu"))
			{
				return false;
			}
		}
		catch(Exception e)
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
		// IS IN GAME
		if(!CoKCore.isPlayerInAnyGame(player))
		{
			CoKChatUtils.printError(player, "You are not in a game!");
			return;
		}
		
		// IS REFEREE
		CoKGame game = CoKCore.getGameByPlayer(player);
		if(!game.isInTeam(player, EnumTeam.REF))
		{
			CoKChatUtils.printError(player, "You are not a referee!");
			return;
		}
		
		// ONLY RUNNING GAMES
		if(!game.isGameOn())
		{
			CoKChatUtils.printError(player, "The game is not running!");
			return;
		}
		
		// CALL NEW ARCHER
		EnumTeam TeamID = EnumTeam.RED;
		if(args[1].equalsIgnoreCase("blu"))
		{
			TeamID = EnumTeam.BLU;
		}
				
		if(game.getArcher(TeamID) != null)
		{
			Player oldLeader = CoKCore.getPlayer(game.getArcher(TeamID));
			game.getArcherClass().clearKit(oldLeader);
		}
		
		game.callNewArcher(TeamID);		
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
