package com.bukkit.gemo.ClashOfKingdoms.commands.all;

import org.bukkit.entity.Player;
import com.bukkit.gemo.ClashOfKingdoms.game.*;
import com.bukkit.gemo.ClashOfKingdoms.*;

public class cmdClose
{
	private static String Description = "Close a game";
	private static String Syntax = "/cok close <gamename>";
	private static String[] Examples = {"/cok close testgame"};
	
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
		
		CoKGame game = CoKCore.GameList.get(args[1]);
		if(!game.gameOwner.equalsIgnoreCase(player.getName()))
		{
			CoKChatUtils.printError(player, "You are not the gameowner!");
			return;
		}
		
		game.closeGame();
		CoKChatUtils.printSuccess(player, "Game '" + args[1] + "' closed!");
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
