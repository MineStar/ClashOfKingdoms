package com.bukkit.gemo.ClashOfKingdoms.commands.all;

import org.bukkit.entity.Player;
import com.bukkit.gemo.ClashOfKingdoms.*;

public class cmdJoin
{
	private static String Description = "Join a CoK-Game";
	private static String Syntax = "/cok join <gamename>";
	private static String[] Examples = {"/cok join testgame"};
	
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
		if(!CoKCore.GameList.containsKey(args[1]))
		{
			CoKChatUtils.printError(player, "There is no such game!");
			return;
		}
		
		if(CoKCore.isPlayerInAnyGame(player))
		{
			CoKChatUtils.printError(player, "You must quit your current game to join another game!");
			return;
		}
		CoKCore.GameList.get(args[1]).joinGame(player);
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
