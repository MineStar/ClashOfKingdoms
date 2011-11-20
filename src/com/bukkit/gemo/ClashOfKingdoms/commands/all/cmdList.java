package com.bukkit.gemo.ClashOfKingdoms.commands.all;

import java.util.Iterator;
import com.bukkit.gemo.ClashOfKingdoms.game.*;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.bukkit.gemo.ClashOfKingdoms.*;

public class cmdList
{
	private static String Description = "List all CoK-Games";
	private static String Syntax = "/cok list";
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
		Iterator<CoKGame> iterator = CoKCore.GameList.values().iterator();
		String list = "";
		while(iterator.hasNext())
		{		
			list += ChatColor.GOLD + iterator.next().gameName + ChatColor.WHITE + ", ";
		}
		player.sendMessage(ChatColor.AQUA + "[Clash of Kingdoms - Current games]");
		player.sendMessage(list);				
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
