package com.bukkit.gemo.ClashOfKingdoms.web.templates;

import com.bukkit.gemo.BukkitHTTP.HTTPEvent;
import com.bukkit.gemo.BukkitHTTP.Page;
import com.bukkit.gemo.ClashOfKingdoms.CoKCore;
import com.bukkit.gemo.ClashOfKingdoms.game.*;

public class tplGameNew extends TemplatePage
{
	public static void execTemplate(Page page, HTTPEvent event)
	{
		StringObject gameList = new StringObject();			
		String topicText 	= "New Game";
		StringObject contentText = new StringObject();
		if(event.isGetMethod)
		{
			append(contentText, getFormTag("data", "post", "newgame.html"), true);
			// SETTINGSNAME
			append(contentText, "Gamename: ", true);
			append(contentText, getInputText("name", "", 20), true);
			appendBR(contentText, 2, true);
			// TOWERHEIGHT
			append(contentText, "Setting:", true);
			append(contentText, getSelectTag("setting"), true);
			for(CoKGameSettings setting : CoKCore.allSettings.values())
			{
				if(!CoKCore.settingsInUse(setting.getSettingsName()) && setting.isComplete())
				{
					append(contentText, getOptionTag(setting.getSettingsName(), setting.getSettingsName(), false), true);
				}
			}
			append(contentText, getCloseTag("select"), true);					
			appendBR(contentText, 2, true);
			append(contentText, getButton("send", "Create game"), true);
			append(contentText, getCloseTag("form"), true);
			appendBR(contentText, 2, true);
		}
		else
		{
			////////////////////////////////
			//
			// CREATE NEW GAME
			//
			////////////////////////////////
			topicText = "New Game";
			contentText.str = "";
			if(event.postParameter != null)
			{
				String name = event.postParameter.get("name");
				String setting = event.postParameter.get("setting");
						
				boolean err = false;
				if(name == null)
				{
					append(contentText, "Please enter a gamename!", true);
					err = true;
				}
				if(name != null)
				{
					name = name.replace("+", "_");
					if(name.length() < 1)
					{
						append(contentText, "Please enter a gamename!", true);
						err = true;
					}
					
					if(CoKCore.GameList.containsKey(name))
					{
						append(contentText, "A game with that name already exists!", true);
						err = true;
					}
					
					if(!CoKCore.allSettings.containsKey(setting))
					{
						append(contentText, "The selected setting was not found!", true);
						err = true;
					}
					
					if(CoKCore.settingsInUse(setting))
					{
						append(contentText, "The selected setting is already in use!", true);
						err = true;
					}
				}

				if(!err)
				{
					String gameOwner = event.getCookieParam("Username");
					if(gameOwner != null)
					{
						CoKGame game = new CoKGame(name, gameOwner);
						CoKCore.GameList.put(game.gameName, game);
						game.loadSettings(CoKCore.allSettings.get(setting));
						contentText.str = "Game '" + game.gameName + "' created with settings '" + game.currentSettings.getSettingsName() + "'!";
					}
					else
					{
						contentText.str = "Could not create the game (Username not found in Database).";
					}
				}
			}
		}
		
		appendGameList(gameList);
		page.replaceText("%LISTOFSETTINGS%", gameList.str);
		page.replaceText("%TOPIC%", topicText);	
		page.replaceText("%CONTENT%", contentText.str);			
	}
}
