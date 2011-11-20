package com.bukkit.gemo.ClashOfKingdoms.web.templates;

import org.bukkit.Material;
import com.bukkit.gemo.ClashOfKingdoms.game.*;
import com.bukkit.gemo.BukkitHTTP.HTTPEvent;
import com.bukkit.gemo.BukkitHTTP.Page;
import com.bukkit.gemo.ClashOfKingdoms.CoKCore;
import com.bukkit.gemo.ClashOfKingdoms.web.TemplateFinder;

public class tplSettingsAddItem extends TemplatePage
{
	public static void execTemplate(Page page, HTTPEvent event)
	{
		StringObject gameList = new StringObject();	
		
		String topicText 	= "Add Item";
		StringObject contentText = new StringObject();
		if(event.isGetMethod)
		{
			if(event.getParameter != null)
			{		
				if(event.getParameter.containsKey("name") && event.getParameter.containsKey("class"))
				{
					String aClass = event.getParameter.get("class");
					CoKGameSettings setting = CoKCore.allSettings.get(event.getParameter.get("name"));
					if(aClass.equalsIgnoreCase("leader") || aClass.equalsIgnoreCase("archer") || aClass.equalsIgnoreCase("knight"))
					{
						if(setting != null)
						{	
							append(contentText, getFormTag("data", "post", "settingsAddItem.html"), true);
							// ITEM ID
							append(contentText, "<b>Item: </b>", true);
							append(contentText, getSelectTag("itemid"), true);
							for(Material mat : Material.values())
							{
								if(mat.getId() > 0)
									append(contentText, getOptionTag(mat.name().replace("_", " "), "" + mat.getId(), mat.getId() == 1), true);
							}					
							append(contentText, getCloseTag("select") + " : ", true);
							// ITEM SUBID
							append(contentText, getSelectTag("itemsubid"), true);
							for(int i = 0; i < 15; i++)
							{
								append(contentText, getOptionTag("" + i, "" + i, i == 0), true);
							}					
							append(contentText, getCloseTag("select"), true);			
							appendBR(contentText, 2, true);
							
							// ITEM AMOUNT
							append(contentText, "<b>Amount: </b>", true);
							append(contentText, getSelectTag("itemamount"), true);
							for(int i = 1; i <= 64; i++)
							{
								append(contentText, getOptionTag("" + i, "" + i, i == 0), true);
							}					
							append(contentText, getCloseTag("select"), true);			
							appendBR(contentText, 2, true);
							
							// BUTTONS
							append(contentText, getInputHidden("name", setting.getSettingsName(), 20), true);
							append(contentText, getInputHidden("class", aClass, 20), true);							
							append(contentText, getButton("send", "Add Item"), true);
							append(contentText, getCloseTag("form"), true);
							appendBR(contentText, 2, true);
						}
					}
					else
					{
						append(contentText, "<font color=\"red\"><b>Unknown playerclass!</b></font>", true);
					}
				}
				else
				{
					contentText.str = "Setting not found!";
				}
			}
			else
			{
				contentText.str = "Setting not found!";
			}
		}
		else
		{
			// SAVE NEW SETTINGS
			topicText = "Add Item";
			contentText.str = "";
			if(event.postParameter != null)
			{
				String name = event.postParameter.get("name");
				String aClass = event.postParameter.get("class");
				int tAmount = Integer.valueOf(event.postParameter.get("itemamount"));
				int tId = Integer.valueOf(event.postParameter.get("itemid"));
				byte tData = Byte.valueOf(event.postParameter.get("itemsubid"));
				
				boolean err = false;
				if(!CoKCore.allSettings.containsKey(name))
				{
					append(contentText, "A setting with that name does not exist!", true);
					err = true;
				}
				else if(!TemplateFinder.isValidSubID(tId, tData))
				{
					err = true;
					append(contentText, "The selected subid is not valid for this Item!", true);
				}
				else if(!aClass.equalsIgnoreCase("leader") && !aClass.equalsIgnoreCase("archer") && !aClass.equalsIgnoreCase("knight"))
				{
					err = true;
					append(contentText, "<font color=\"red\"><b>Unknown playerclass!</b></font>", true);
				}
				else if(CoKCore.settingsInUseRunning(name))
				{
					err = true;
					append(contentText, "<font color=\"red\"><b>The selected setting is used in a running game!</b></font>", true);
				}
				
				if(!err)
				{					
					CoKClassSettings thisSetting = CoKCore.allSettings.get(name).getLeaderSettings();
					if(aClass.equalsIgnoreCase("archer"))
					{
						thisSetting = CoKCore.allSettings.get(name).getArcherSettings();
					}
					else if(aClass.equalsIgnoreCase("knight"))
					{
						thisSetting = CoKCore.allSettings.get(name).getKnightSettings();
					}
					thisSetting.addItem(new CoKItemStack(tId, tData, tAmount));
					CoKCore.saveGameSettings();
					contentText.str = "<font color=\"green\"><b>Item '" + tAmount + " x " + Material.getMaterial(tId).name().replace("_", " ") + (tData > 0 ? ":" + tData : "") +"' added to '" + aClass + "'!</b></font>";
					appendBR(contentText, 2, true);
					append(contentText, getLink("Back to the settings", "settings.html?name=" + name), true);
				}				
			}
		}
		
		appendSettingsList(gameList);
		page.replaceText("%LISTOFSETTINGS%", gameList.str);
		page.replaceText("%TOPIC%", topicText);	
		page.replaceText("%CONTENT%", contentText.str);			
	}
}
