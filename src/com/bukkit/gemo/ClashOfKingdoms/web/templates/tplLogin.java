package com.bukkit.gemo.ClashOfKingdoms.web.templates;

import com.bukkit.gemo.BukkitHTTP.HTTPEvent;
import com.bukkit.gemo.BukkitHTTP.Page;

public class tplLogin extends TemplatePage
{
	public static void execTemplate(Page page, HTTPEvent event)
	{
		StringObject gameList = new StringObject();		
		appendGameList(gameList);		
		page.replaceText("%MESSAGE%", "");
		page.replaceText("%LISTOFGAMES%", gameList.str);
	}
}
