package com.bukkit.gemo.ClashOfKingdoms.commands.ref;

import org.bukkit.entity.Player;
import com.bukkit.gemo.ClashOfKingdoms.game.*;
import com.bukkit.gemo.ClashOfKingdoms.*;

public class cmdSpawn
{
	private static String Description = "Set the spawn";
	private static String Syntax = "/cok spawn red|blu|ref|pun";
	private static String[] Examples = {};
	
	// CHECK SYNTAX
	public static boolean checkSyntax(String[] args)
	{
		if(args.length != 2)
		{
			return false;
		}
		
		if(!args[1].equalsIgnoreCase("red")
				&& !args[1].equalsIgnoreCase("blu")
				&& !args[1].equalsIgnoreCase("ref")
				&& !args[1].equalsIgnoreCase("pun"))					
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
		
		if(args[1].equalsIgnoreCase("red"))					
		{					
			game.currentSettings.setSpawnRed(new CoKLocation(player.getLocation()));	
		}
		else if(args[1].equalsIgnoreCase("blu"))					
		{					
			game.currentSettings.setSpawnBlu(new CoKLocation(player.getLocation()));	
		}
		else if(args[1].equalsIgnoreCase("ref"))					
		{					
			game.currentSettings.setSpawnRef(new CoKLocation(player.getLocation()));	
		}
		else if(args[1].equalsIgnoreCase("pun"))					
		{					
			game.currentSettings.setSpawnPun(new CoKLocation(player.getLocation()));	
		}
		CoKChatUtils.printSuccess(player, "Spawn set!");
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
