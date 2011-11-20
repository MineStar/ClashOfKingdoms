package com.bukkit.gemo.ClashOfKingdoms.commands.ref;

import org.bukkit.entity.Player;
import com.bukkit.gemo.ClashOfKingdoms.game.*;
import com.bukkit.gemo.ClashOfKingdoms.*;

public class cmdSettingsMaterial
{
	private static String Description = "Set the towermaterial";
	private static String Syntax = "/cok settings material <ID[:SubID]>";
	private static String[] Examples = {"/cok settings material 1", "/cok settings material 35:2"};
	
	// CHECK SYNTAX
	public static boolean checkSyntax(String[] args)
	{
		if(args.length != 3)
		{
			return false;
		}
		try
		{
			String[] split = args[2].split(":");
			if(split.length == 1)
			{
				Integer.valueOf(split[0]);
			}
			else if(split.length == 2)
			{
				Integer.valueOf(split[0]);
				Byte.valueOf(split[1]);
			}
			else 
				return false;
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
		
		String[] split = args[2].split(":");
		if(split.length == 1)
		{
			game.currentSettings.setTowerID(Integer.valueOf(split[0]));
			game.currentSettings.setTowerData((byte)0);			
		}
		else if(split.length == 2)
		{
			game.currentSettings.setTowerID(Integer.valueOf(split[0]));
			game.currentSettings.setTowerData(Byte.valueOf(split[1]));		
		}
		
		CoKChatUtils.printSuccess(player, "Changed towermaterial to: " + game.currentSettings.getTowerID() + ":" + game.currentSettings.getTowerData() + ".");
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
