package com.bukkit.gemo.ClashOfKingdoms.commands.all;

import org.bukkit.entity.Player;
import com.bukkit.gemo.ClashOfKingdoms.game.*;
import com.bukkit.gemo.ClashOfKingdoms.*;

public class cmdSwitchTeam
{
	private static String Description = "Join a team";
	private static String Syntax = "/cok red|blu|ref|none";
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
		
		EnumTeam TeamID = EnumTeam.NONE;
		if(args[0].equalsIgnoreCase("RED"))
			TeamID = EnumTeam.RED;
		else if(args[0].equalsIgnoreCase("BLU"))
			TeamID = EnumTeam.BLU;
		if(args[0].equalsIgnoreCase("REF"))
			TeamID = EnumTeam.REF;		
		
		CoKCore.getGameByPlayer(player).joinTeam(player, TeamID);			
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
