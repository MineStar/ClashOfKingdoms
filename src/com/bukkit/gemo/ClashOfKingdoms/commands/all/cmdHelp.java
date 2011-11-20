package com.bukkit.gemo.ClashOfKingdoms.commands.all;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.bukkit.gemo.ClashOfKingdoms.*;
import com.bukkit.gemo.ClashOfKingdoms.commands.ref.*;

public class cmdHelp 
{
	private static String Description = "Show CoK-Help";
	private static String Syntax = "/cok help [page]";
	private static String[] Examples = {"/cok help", "/cok help 2"};
	
	// CHECK SYNTAX
	public static boolean checkSyntax(String[] args)
	{
		if(args.length < 1 || args.length > 2)
		{
			return false;
		}
		
		if(args.length == 2)
		{
			try
			{
				Integer.valueOf(args[1]);
			}
			catch(Exception e)
			{
				return false;
			}
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
		printHelp(player, args);
		return;		
	}
	
	public static void printHelp(Player player, String[] args)
	{
		int page = 1;
		if(args.length == 2)
			page = Integer.valueOf(args[1]);
		
		if(page < 0)
			page = 1;
		if(page > 4)
			page = 4;
		
		CoKChatUtils.printLine(player, ChatColor.AQUA, " ----[ Clash of Kingdoms Help " + page + "/4 ] ----");		
		if(page == 1)
		{
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdBase.getSyntax() 	+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdBase.getDescription());		
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdClose.getSyntax() 	+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdClose.getDescription());		
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdCreate.getSyntax() 	+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdCreate.getDescription());		
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdDepunish.getSyntax() 	+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdDepunish.getDescription());		
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdJoin.getSyntax() 	+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdJoin.getDescription());		
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdList.getSyntax() 	+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdList.getDescription());		
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdMovePlayer.getSyntax() 	+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdMovePlayer.getDescription());		
		}
		else if(page == 2)
		{
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdPunish.getSyntax() 			+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdPunish.getDescription());		
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdPunishTeam.getSyntax() 		+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdPunishTeam.getDescription());		
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdQuit.getSyntax() 			+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdQuit.getDescription());		
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdRefreshLeader.getSyntax() 	+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdRefreshLeader.getDescription());		
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdReset.getSyntax() 			+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdReset.getDescription());		
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdSettingsCreate.getSyntax() 	+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdSettingsCreate.getDescription());		
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdSettingsDelete.getSyntax() 	+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdSettingsDelete.getDescription());		
		}
		else if(page == 3)
		{
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdSettingsHeight.getSyntax() 	+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdSettingsHeight.getDescription());		
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdSettingsList.getSyntax() 	+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdSettingsList.getDescription());		
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdSettingsLoad.getSyntax() 	+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdSettingsLoad.getDescription());		
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdSettingsMaterial.getSyntax() + ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdSettingsMaterial.getDescription());		
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdSettingsSave.getSyntax() 	+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdSettingsSave.getDescription());		
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdShowScore.getSyntax() 		+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdShowScore.getDescription());		
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdSpawn.getSyntax() 			+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdSpawn.getDescription());		
		}
		else if(page == 4)
		{
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdStart.getSyntax() 			+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdStart.getDescription());		
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdStop.getSyntax() 			+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdStop.getDescription());		
			CoKChatUtils.printLine(player, ChatColor.GRAY, cmdSwitchTeam.getSyntax() 		+ ChatColor.WHITE + " : " + ChatColor.DARK_GREEN + cmdSwitchTeam.getDescription());		
		}
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
