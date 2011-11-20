package com.bukkit.gemo.ClashOfKingdoms.web.templates;

import org.bukkit.Material;
import com.bukkit.gemo.ClashOfKingdoms.game.*;
import com.bukkit.gemo.BukkitHTTP.HTTPEvent;
import com.bukkit.gemo.BukkitHTTP.Page;
import com.bukkit.gemo.ClashOfKingdoms.CoKCore;
import com.bukkit.gemo.ClashOfKingdoms.web.TemplateFinder;

public class tplSettingsNew extends TemplatePage
{
	public static void execTemplate(Page page, HTTPEvent event)
	{
		StringObject gameList = new StringObject();	
		
		String topicText 	= "New Setting";
		StringObject contentText = new StringObject();
		if(event.isGetMethod)
		{
			append(contentText, getFormTag("data", "post", "newsetting.html"), true);
			// SETTINGSNAME
			append(contentText, "Name: ", true);
			append(contentText, getInputText("name", "", 20), true);
			appendBR(contentText, 2, true);
			// TOWERHEIGHT
			append(contentText, "Towerheight:", true);
			append(contentText, getSelectTag("towerheight"), true);
			for(int i = 1; i <= 20; i++)
			{
				append(contentText, getOptionTag("" + i, "" + i, i == 3), true);
			}					
			append(contentText, getCloseTag("select"), true);					
			appendBR(contentText, 2, true);
	
			// TOWERMATERIAL
			append(contentText, "Towermaterial:", true);
			append(contentText, getSelectTag("towerid"), true);	
			for(Material mat : Material.values())
			{						
				if(TemplateFinder.isValidBlock(mat.getId()))
				{
					append(contentText, getOptionTag(mat.name(), "" + mat.getId(), mat.getId()== 1), true);
				}
			}					
			append(contentText, getCloseTag("select") + ":", true);					
	
			// TOWERDATA
			append(contentText, getSelectTag("towerdata"), true);	
			for(int i = 0; i <= 15; i++)
			{
				append(contentText, getOptionTag("" + i, "" + i, i == 0), true);
			}					
			append(contentText, getCloseTag("select"), true);		
			appendBR(contentText, 2, true);
			append(contentText, getButton("send", "Save changes"), true);
			append(contentText, getCloseTag("form"), true);
			appendBR(contentText, 2, true);
		}
		else
		{
			// SAVE NEW SETTINGS
			topicText = "Settings";
			contentText.str = "";
			if(event.postParameter != null)
			{
				String name = event.postParameter.get("name");
				name = name.replace("+", "_");
				int tHeight = Integer.valueOf(event.postParameter.get("towerheight"));
				int tId = Integer.valueOf(event.postParameter.get("towerid"));
				byte tData = Byte.valueOf(event.postParameter.get("towerdata"));
				
				boolean err = false;
				if(CoKCore.allSettings.containsKey(name))
				{
					append(contentText, "A setting with that name already exists!", true);
					err = true;
				}
				
				if(CoKCore.allSettings.containsKey(name))
				{
					append(contentText, "A setting with that name already exists!", true);
					err = true;
				}
				else if(!TemplateFinder.isValidBlock(tId))
				{
					err = true;
					append(contentText, "The selected blocktype is not a valid block!", true);
				}
				else if(!TemplateFinder.isValidSubID(tId, tData))
				{
					err = true;
					append(contentText, "The selected subid is not valid for this blocktype!", true);
				}
				
				if(!err)
				{
					CoKGameSettings set = new CoKGameSettings(name);
					set.setTowerHeight(tHeight);
					set.setTowerData(tData);
					set.setTowerID(tId);
					CoKCore.allSettings.put(set.getSettingsName(), set);
					CoKCore.saveGameSettings();
					contentText.str = "Settings '" + set.getSettingsName() + "' created!";
				}
			}
		}
		
		appendSettingsList(gameList);
		page.replaceText("%LISTOFSETTINGS%", gameList.str);
		page.replaceText("%TOPIC%", topicText);	
		page.replaceText("%CONTENT%", contentText.str);			
	}
}
