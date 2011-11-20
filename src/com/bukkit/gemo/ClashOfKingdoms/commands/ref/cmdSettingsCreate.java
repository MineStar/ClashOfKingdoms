package com.bukkit.gemo.ClashOfKingdoms.commands.ref;

import org.bukkit.entity.Player;
import com.bukkit.gemo.ClashOfKingdoms.game.*;
import com.bukkit.gemo.ClashOfKingdoms.*;

public class cmdSettingsCreate
{
	private static String Description = "Create new gamesettings";
	private static String Syntax = "/cok settings create <settingsname>";
	private static String[] Examples = {"/cok settings create hill711"};
	
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
		if(CoKCore.allSettings.containsKey(args[2]))
		{
			CoKChatUtils.printError(player, "Settings do already exist!");
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

		// GAME RUNNING?
		if(game.gameOn)
		{
			CoKChatUtils.printError(player, "Cannot modify running games.");
			return;
		}
		
		game.currentSettings = new CoKGameSettings(args[2]);
		CoKCore.allSettings.put(game.currentSettings.getSettingsName(), game.currentSettings);
		CoKChatUtils.printSuccess(player, "Created settings '" + game.currentSettings.getSettingsName() + "'.");
		CoKChatUtils.printSuccess(player, "Current settings: '" + game.currentSettings.getSettingsName() + "'.");
		CoKCore.saveGameSettings();
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
