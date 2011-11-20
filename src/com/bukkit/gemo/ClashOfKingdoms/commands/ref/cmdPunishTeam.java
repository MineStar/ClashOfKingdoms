package com.bukkit.gemo.ClashOfKingdoms.commands.ref;

import org.bukkit.entity.Player;
import com.bukkit.gemo.ClashOfKingdoms.game.*;
import com.bukkit.gemo.ClashOfKingdoms.*;

public class cmdPunishTeam
{
	private static String Description = "Punish a team";
	private static String Syntax = "/cok punishteam red|blu <blockcount>";
	private static String[] Examples = {"/cok punishteam blu 4", "/cok punishteam red 2"};
	
	// CHECK SYNTAX
	public static boolean checkSyntax(String[] args)
	{
		if(args.length != 3)
		{
			return false;
		}
		
		try
		{
			Integer.valueOf(args[2]);
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
		
		// PUNISH TEAM
		game.punishTeam(args[1], Integer.valueOf(args[2]));			
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
