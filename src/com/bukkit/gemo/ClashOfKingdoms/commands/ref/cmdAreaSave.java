package com.bukkit.gemo.ClashOfKingdoms.commands.ref;

import org.bukkit.entity.Player;

import com.bukkit.gemo.ClashOfKingdoms.Listener.CoKPL;
import com.bukkit.gemo.ClashOfKingdoms.game.*;
import com.bukkit.gemo.ClashOfKingdoms.*;

public class cmdAreaSave
{
	private static String Description = "Save the gamearea";
	private static String Syntax = "/cok savearea";
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
		
		// IS GAME ON
		if(game.isGameOn())
		{
			CoKChatUtils.printError(player, "Game is already running!");
			return;
		}
		
		// IS SETTING COMPLETE
		CoKSelectArea select = CoKPL.Selections.get(player.getName());
		if(select == null)
		{
			CoKChatUtils.printError(player, "No Area selected!");
			return;
		}
		else if(select.getSize() != 2)
		{
			CoKChatUtils.printError(player, "You must select two points!");
			return;
		}
		
		//System.out.println(AreaDataHandler.exportSchematic(game.currentSettings.getSettingsName(),select.getLocList().get(0).getBlock().getChunk(), select.getLocList().get(1).getBlock().getChunk()));
		//Chunk c1 = select.getLocList().get(0).getWorld().getChunkAt(19, 20);
		//Chunk c2 = select.getLocList().get(1).getWorld().getChunkAt(22, 22);
		//System.out.println(AreaDataHandler.resetArea(game.currentSettings.getSettingsName(), c1, c2));
        
		
		CoKGameArea gameArea = new CoKGameArea(select.getLocList().get(0), select.getLocList().get(1));
		gameArea.writeToFile(game.currentSettings.getSettingsName());
		CoKChatUtils.printSuccess(player, "Gamearea set!");		
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
