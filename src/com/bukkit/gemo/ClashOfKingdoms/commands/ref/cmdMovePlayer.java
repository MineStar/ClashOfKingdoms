package com.bukkit.gemo.ClashOfKingdoms.commands.ref;

import org.bukkit.entity.Player;
import com.bukkit.gemo.ClashOfKingdoms.*;
import com.bukkit.gemo.ClashOfKingdoms.game.*;

public class cmdMovePlayer
{
	private static String Description = "Move a player to a team";
	private static String Syntax = "/cok move <playername> red|blu|ref|none";
	private static String[] Examples = {"/cok move ezio blu"};
	
	// CHECK SYNTAX
	public static boolean checkSyntax(String[] args)
	{
		if(args.length != 3)
		{
			return false;
		}
		
		if(!args[2].equalsIgnoreCase("red") && !args[2].equalsIgnoreCase("blu") && !args[2].equalsIgnoreCase("ref") && !args[2].equalsIgnoreCase("none"))
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
		
		Player moved = CoKCore.getPlayer(args[1]);
		if(moved == null)
		{
			CoKChatUtils.printError(player, "Player not found!");
			return;
		}
		if(!CoKCore.getGameByPlayer(moved).gameName.equalsIgnoreCase(game.gameName))
		{
			CoKChatUtils.printError(player, "This player is not in this game!");
			return;
		}
		
		EnumTeam TeamID = EnumTeam.NONE;
		if(args[0].equalsIgnoreCase("RED"))
			TeamID = EnumTeam.RED;
		else if(args[0].equalsIgnoreCase("BLU"))
			TeamID = EnumTeam.BLU;
		if(args[0].equalsIgnoreCase("REF"))
			TeamID = EnumTeam.REF;	
		
		game.movePlayer(moved, TeamID);
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
