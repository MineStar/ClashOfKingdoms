package com.bukkit.gemo.ClashOfKingdoms.commands.ref;

import org.bukkit.entity.Player;
import com.bukkit.gemo.ClashOfKingdoms.game.*;
import com.bukkit.gemo.ClashOfKingdoms.*;

public class cmdPunish
{
	private static String Description = "Punish a player";
	private static String Syntax = "/cok punish <playername>";
	private static String[] Examples = {"/cok punish krababbel"};
	
	// CHECK SYNTAX
	public static boolean checkSyntax(String[] args)
	{
		if(args.length != 2)
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
		
		Player punished = CoKCore.getPlayer(args[1]);
		if(punished == null)
		{
			CoKChatUtils.printError(player, "Player not found!");
			return;
		}
		if(!CoKCore.getGameByPlayer(punished).gameName.equalsIgnoreCase(game.gameName))
		{
			CoKChatUtils.printError(player, "This player is not in this game!");
			return;
		}
		
		if(game.currentSettings.getSpawnPun() != null)
		{
			game.punishPlayer(punished);
			return;
		}
		else
		{
			CoKChatUtils.printError(player, "No punishspawn set!");
			return;
		}
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
