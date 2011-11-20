package com.bukkit.gemo.ClashOfKingdoms.web;

import com.bukkit.gemo.BukkitHTTP.HTTPEvent;
import com.bukkit.gemo.BukkitHTTP.Page;
import com.bukkit.gemo.ClashOfKingdoms.web.templates.*;

public class TemplateFinder
{
	public static Page findTemplate(Page page, HTTPEvent event)
	{
		if(event.fileName.equalsIgnoreCase("ERROR404.html"))
		{
			tplError404.execTemplate(page, event);
		}
		else if(event.fileName.equalsIgnoreCase("index.html"))
		{
			tplIndex.execTemplate(page, event);
			page.replaceText("%USERNAME%", event.getCookieParam("Username"));
		}
		else if(event.fileName.equalsIgnoreCase("newgame.html"))
		{
			tplGameNew.execTemplate(page, event);
			page.replaceText("%USERNAME%", event.getCookieParam("Username"));
		}
		else if(event.fileName.equalsIgnoreCase("settings.html"))
		{
			tplSettings.execTemplate(page, event);
			page.replaceText("%USERNAME%", event.getCookieParam("Username"));
		}
		else if(event.fileName.equalsIgnoreCase("newsetting.html"))
		{
			tplSettingsNew.execTemplate(page, event);
			page.replaceText("%USERNAME%", event.getCookieParam("Username"));
		}
		else if(event.fileName.equalsIgnoreCase("settingsAddItem.html"))
		{
			tplSettingsAddItem.execTemplate(page, event);
			page.replaceText("%USERNAME%", event.getCookieParam("Username"));
		}
		else if(event.fileName.equalsIgnoreCase("login.html"))
		{
			tplLogin.execTemplate(page, event);
		}		
		
		
		
		return page;
	}
	
	public static boolean isValidBlock(int id)
	{	
		if(id == 1
				|| id == 3
				|| id == 4
				|| id == 5
				|| id == 7
				|| (id >= 12 && id <= 25)
				|| id == 35
				|| id == 41
				|| id == 42
				|| id == 43
				|| id == 44
				|| id == 45
				|| id == 46
				|| id == 47
				|| id == 48
				|| id == 49
				|| id == 53
				|| id == 56
				|| id == 57
				|| id == 58
				|| id == 73
				|| id == 74
				|| id == 78
				|| id == 79
				|| id == 80
				|| id == 82
				|| id == 84
				|| id == 86
				|| id == 87
				|| id == 88
				|| id == 89
				|| id == 91
				)
			return true;
		
		return false;
	}
	
	public static boolean isValidSubID(int id, int subid)
	{	
		if(id != 17 && id != 18 && id != 35 && id != 43 && id != 44 && subid > 0)
			return false;
		
		if(id == 17 || id == 18)
		{
			if(subid > 2 || subid < 0)
				return false;
		}
		else if(id == 35)
		{
			if(subid > 15 || subid < 0)
				return false;
		}
		else if(id == 43 || id == 44)
		{
			if(subid > 3 || subid < 0)
				return false;
		}
		
		return true;
	}
	
}
