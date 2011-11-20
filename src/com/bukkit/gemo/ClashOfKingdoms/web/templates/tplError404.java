package com.bukkit.gemo.ClashOfKingdoms.web.templates;

import com.bukkit.gemo.BukkitHTTP.HTTPEvent;
import com.bukkit.gemo.ClashOfKingdoms.game.*;
import com.bukkit.gemo.BukkitHTTP.Page;
import com.bukkit.gemo.ClashOfKingdoms.CoKCore;

public class tplError404 extends TemplatePage
{
	public static void execTemplate(Page page, HTTPEvent event)
	{
		String gameList = "<ul>";		
		for(CoKGame game : CoKCore.GameList.values())
		{
			gameList += "<li><a href=\"showgame.html?name=" + game.gameName + "\">" + game.gameName + "</a></li>";
		}		
		gameList += "</ul>";
		
		String contentText 	= "ERROR 404 - The requested file was not found";
		
		page.replaceText("%LISTOFGAMES%", gameList);
		page.replaceText("%ERROR%", contentText);	
	}
}
