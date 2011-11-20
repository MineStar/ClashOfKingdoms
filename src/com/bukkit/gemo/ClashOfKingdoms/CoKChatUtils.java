package com.bukkit.gemo.ClashOfKingdoms;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CoKChatUtils
{
	public static void printLine(Player player, ChatColor color, String message)
	{
		player.sendMessage(color + message);
	}
	
	public static void printInfo(Player player, ChatColor color, String message)
	{
		player.sendMessage(ChatColor.AQUA + "[CoK] " + color + message);
	}
	
	public static void printError(Player player, String message)
	{
		printInfo(player, ChatColor.RED, message);
	}
	
	public static void printSuccess(Player player, String message)
	{
		printInfo(player, ChatColor.GREEN, message);
	}
	
	// WRONG SYNTAX
	public static void printWrongSyntax(Player player, String Syntax, String[] Examples)
	{
		CoKChatUtils.printError(player, "Wrong Syntax! Use: " + Syntax);	
		
		if(Examples.length == 1)
			CoKChatUtils.printInfo(player, ChatColor.GRAY, "Example:");	
		else if(Examples.length > 1)
			CoKChatUtils.printInfo(player, ChatColor.DARK_RED, "Examples:");	
		
		for(int i = 0; i < Examples.length; i++)
		{
			CoKChatUtils.printInfo(player, ChatColor.GRAY, Examples[i]);
		}
		return;
	}
}
