package com.bukkit.gemo.ClashOfKingdoms.commands.ref;

import org.bukkit.entity.Player;
import com.bukkit.gemo.ClashOfKingdoms.game.*;
import com.bukkit.gemo.ClashOfKingdoms.*;

public class cmdSettingsDelete
{
	private static String Description = "Delete gamesettings";
	private static String Syntax = "/cok settings delete <settingsname>";
	private static String[] Examples = {"/cok settings delete hill711"};
	
	// CHECK SYNTAX
	public static boolean checkSyntax(String[] args)
	{
		if(args.length != 3)
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
		if(!CoKCore.allSettings.containsKey(args[2]))
		{
			CoKChatUtils.printError(player, "There are no such settings!");
			return;
		}
		
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
		
		// SETTINGS IN USE?
		if(CoKCore.settingsInUse(args[2]))
		{
			CoKChatUtils.printError(player, "Gamesettings are currently in use.");
			return;
		}
		
		// GAME RUNNING?
		if(game.gameOn)
		{
			CoKChatUtils.printError(player, "Cannot modify running games.");
			return;
		}
		
		game.currentSettings = new CoKGameSettings("default");
		CoKCore.allSettings.remove(args[2]);
		CoKChatUtils.printSuccess(player, "Deleted settings '" + args[2] + "'.");
		CoKChatUtils.printSuccess(player, "Current settings: '" + game.currentSettings.getSettingsName() + "'.");
		CoKCore.saveGameSettings();
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
