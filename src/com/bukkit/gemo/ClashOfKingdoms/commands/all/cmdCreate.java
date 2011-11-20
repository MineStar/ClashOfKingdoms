package com.bukkit.gemo.ClashOfKingdoms.commands.all;

import org.bukkit.entity.Player;
import com.bukkit.gemo.ClashOfKingdoms.game.*;
import com.bukkit.gemo.ClashOfKingdoms.*;

public class cmdCreate
{
	private static String Description = "Create a new game";
	private static String Syntax = "/cok create <gamename>";
	private static String[] Examples = {"/cok create testgame"};
	
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
		if(CoKCore.GameList.containsKey(args[1]))
		{
			CoKChatUtils.printError(player, "This gamename is already in use!");
			return;
		}
		
		if(CoKCore.isPlayerInAnyGame(player))
		{
			CoKChatUtils.printError(player, "You must quit your current game to create another game!");
			return;
		}
		
		CoKCore.GameList.put(args[1], new CoKGame(args[1], player.getName()));
		CoKChatUtils.printSuccess(player, "Game '" + args[1] + "' created!");
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
