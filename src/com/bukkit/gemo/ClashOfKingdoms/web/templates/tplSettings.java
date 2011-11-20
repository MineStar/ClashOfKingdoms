package com.bukkit.gemo.ClashOfKingdoms.web.templates;

import java.util.ArrayList;

import org.bukkit.Material;
import com.bukkit.gemo.BukkitHTTP.HTTPEvent;
import com.bukkit.gemo.BukkitHTTP.Page;
import com.bukkit.gemo.ClashOfKingdoms.CoKCore;
import com.bukkit.gemo.ClashOfKingdoms.web.TemplateFinder;
import com.bukkit.gemo.ClashOfKingdoms.game.*;

public class tplSettings extends TemplatePage
{
	public static void execTemplate(Page page, HTTPEvent event)
	{
		StringObject gameList = new StringObject();	
		appendSettingsList(gameList);
		
		StringObject postText = new StringObject();		
		if(!event.isGetMethod)
		{
			if(event.postParameter != null)
			{
				CoKGameSettings setting = CoKCore.allSettings.get(event.postParameter.get("name"));
				if(setting != null)
				{
					clearText(postText);
					boolean tLeader = Boolean.valueOf(event.postParameter.get("leader"));					
					boolean tArcher = Boolean.valueOf(event.postParameter.get("archer"));
					boolean tKnight = Boolean.valueOf(event.postParameter.get("knight"));
					int tHeight = Integer.valueOf(event.postParameter.get("towerheight"));
					int tId = Integer.valueOf(event.postParameter.get("towerid"));
					byte tData = Byte.valueOf(event.postParameter.get("towerdata"));
					
					int tLeaderHelmet = Integer.valueOf(event.postParameter.get("leaderHelmet"));
					int tLeaderChestplate = Integer.valueOf(event.postParameter.get("leaderChestplate"));
					int tLeaderLeggings = Integer.valueOf(event.postParameter.get("leaderLeggings"));
					int tLeaderBoots = Integer.valueOf(event.postParameter.get("leaderBoots"));
					
					int tArcherHelmet = Integer.valueOf(event.postParameter.get("archerHelmet"));
					int tArcherChestplate = Integer.valueOf(event.postParameter.get("archerChestplate"));
					int tArcherLeggings = Integer.valueOf(event.postParameter.get("archerLeggings"));
					int tArcherBoots = Integer.valueOf(event.postParameter.get("archerBoots"));
					
					int tKnightHelmet = Integer.valueOf(event.postParameter.get("knightHelmet"));
					int tKnightChestplate = Integer.valueOf(event.postParameter.get("knightChestplate"));
					int tKnightLeggings = Integer.valueOf(event.postParameter.get("knightLeggings"));
					int tKnightBoots = Integer.valueOf(event.postParameter.get("knightBoots"));
				
					boolean err = false;
					if(!TemplateFinder.isValidBlock(tId))
					{
						err = true;
						append(postText, "<font color=\"red\">The selected blocktype is not a valid block!</font>", true);
					}
					else if(!TemplateFinder.isValidSubID(tId, tData))
					{
						err = true;
						append(postText, "<font color=\"red\">The selected subid is not valid for this blocktype!</font>", true);
					}
					else if(CoKCore.settingsInUseRunning(setting.getSettingsName()))
					{
						err = true;
						append(postText, "<font color=\"red\">The selected setting is used in a running game!</font>", true);
					}

					if(!err)
					{
						setting.setLeaderAllowed(tLeader);
						setting.setArcherAllowed(tArcher);
						setting.setKnightAllowed(tKnight);
						setting.setTowerID(tId);
						setting.setTowerData(tData);
						setting.setTowerHeight(tHeight);
						// SET LEADER
						setting.getLeaderSettings().setHelmetId(tLeaderHelmet);
						setting.getLeaderSettings().setChestPlateId(tLeaderChestplate);
						setting.getLeaderSettings().setLeggingsId(tLeaderLeggings);
						setting.getLeaderSettings().setBootsId(tLeaderBoots);
						// SET ARCHER
						setting.getArcherSettings().setHelmetId(tArcherHelmet);
						setting.getArcherSettings().setChestPlateId(tArcherChestplate);
						setting.getArcherSettings().setLeggingsId(tArcherLeggings);
						setting.getArcherSettings().setBootsId(tArcherBoots);
						// SET KNIGHT
						setting.getKnightSettings().setHelmetId(tKnightHelmet);
						setting.getKnightSettings().setChestPlateId(tKnightChestplate);
						setting.getKnightSettings().setLeggingsId(tKnightLeggings);
						setting.getKnightSettings().setBootsId(tKnightBoots);
						CoKCore.saveGameSettings();
						postText.str = "<font color=\"green\"><b>Settings saved!</b></font><br /><br />";
					}
				}
			}
		}
		
		String topicText 	= "Settingsoverview";
		StringObject contentText = new StringObject();
		contentText.str = "Please select a setting on the left side or <a href=\"newsetting.html\">CREATE A NEW SETTING</a>.";
		if(event.getParameter != null)
		{		
			if(event.getParameter.containsKey("name"))
			{
				CoKGameSettings setting = CoKCore.allSettings.get(event.getParameter.get("name"));
				if(setting != null)
				{	
					topicText = "Setting: '" + setting.getSettingsName() + "'";
					clearText(contentText);					
					
					if(event.getParameter.containsKey("class") && event.getParameter.containsKey("deleteItem"))
					{
						if(CoKCore.settingsInUseRunning(setting.getSettingsName()))
						{
							append(postText, "<font color=\"red\">The selected setting is used in a running game!</font>", true);
						}
						else
						{
							String aClass = event.getParameter.get("class");
							int aId = Integer.valueOf(event.getParameter.get("deleteItem"));
							
							if(aClass.equalsIgnoreCase("leader") || aClass.equalsIgnoreCase("archer") || aClass.equalsIgnoreCase("knight"))
							{
								CoKClassSettings thisSetting = setting.getLeaderSettings();
								if(aClass.equalsIgnoreCase("archer"))
								{
									thisSetting = setting.getArcherSettings();
								}
								else if(aClass.equalsIgnoreCase("knight"))
								{
									thisSetting = setting.getKnightSettings();
								}
								for(int i = 0; i < thisSetting.getItemList().size(); i++)
								{
									if(aId == i)
									{
										append(contentText, "<font color=\"greed\"><b>Item '" + Material.getMaterial(thisSetting.getItemList().get(aId).getId()).name().replace("_", " ") + "' deleted from playerclass '" + aClass + "'!</b></font>", true);
										thisSetting.getItemList().remove(aId);
										break;
									}
								}	
								CoKCore.saveGameSettings();
							}
							else
							{
								append(contentText, "<font color=\"red\"><b>Unknown playerclass!</b></font>", true);
							}
						}
						appendBR(contentText, 2, true);
					}

					append(contentText, "<b>Red spawn set:</b> " + (setting.getSpawnRed() != null), true);
					appendBR(contentText, 2, true);
					append(contentText, "<b>Blu spawn set:</b> " + (setting.getSpawnBlu() != null), true);
					appendBR(contentText, 2, true);
					append(contentText, "<b>Referee spawn set:</b> " + (setting.getSpawnRef() != null), true);
					appendBR(contentText, 2, true);
					append(contentText, "<b>Punish spawn set:</b> " + (setting.getSpawnPun() != null), true);
					appendBR(contentText, 2, true);
					
					// FORM
					append(contentText, getFormTag("data", "post", "settings.html?name=" + setting.getSettingsName()), true);
					// TOWERHEIGHT
					append(contentText, "<b>Towerheight:</b>", true);
					append(contentText, getSelectTag("towerheight"), true);
					for(int i = 1; i <= 20; i++)
					{
						append(contentText, getOptionTag("" + i, "" + i, i == setting.getTowerHeight()), true);
					}					
					append(contentText, getCloseTag("select"), true);					
					appendBR(contentText, 2, true);

					// TOWERMATERIAL
					append(contentText, "<b>Towermaterial:</b>", true);
					append(contentText, getSelectTag("towerid"), true);	
					for(Material mat : Material.values())
					{						
						if(TemplateFinder.isValidBlock(mat.getId()))
						{
							append(contentText, getOptionTag(mat.name(), "" + mat.getId(), mat.getId()== setting.getTowerID()), true);
						}
					}					
					append(contentText, getCloseTag("select") + ":", true);					

					// TOWERDATA
					append(contentText, getSelectTag("towerdata"), true);	
					for(int i = 0; i <= 15; i++)
					{
						append(contentText, getOptionTag("" + i, "" + i, i == setting.getTowerData()), true);
					}					
					append(contentText, getCloseTag("select"), true);		
					appendBR(contentText, 2, true);					
					
					append(contentText, "<span onclick=\"anzeigen('leader')\"><b>Show Leadersettings</b></span>&nbsp;&nbsp;&nbsp;", true);
					append(contentText, "<span onclick=\"anzeigen('archer')\"><b>Show Archersettings</b></span>&nbsp;&nbsp;&nbsp;", true);
					append(contentText, "<span onclick=\"anzeigen('knight')\"><b>Show Knightsettings</b></span><br /><br />", true);
					// LEADER
					append(contentText, "<span id=\"leader\" style=\"display: none;\">", true);
					append(contentText, "<b>Leadersettings:</b><br />", true);
					append(contentText, "Use:", true);
					append(contentText, getSelectTag("leader"), true);
					append(contentText, getOptionTag("true", "true", setting.isLeaderAllowed()), true);					
					append(contentText, getOptionTag("false", "false", !setting.isLeaderAllowed()), true);	
					append(contentText, getCloseTag("select"), true);					
					appendBR(contentText, 1, true);
					appendClassSettings(contentText, "leader", setting, setting.getLeaderSettings());	
					append(contentText, "</span>", true);
					
					// ARCHER
					append(contentText, "<span id=\"archer\" style=\"display: none;\">", true);
					append(contentText, "<b>Archersettings:</b><br />", true);
					append(contentText, "Use:", true);
					append(contentText, getSelectTag("archer"), true);
					append(contentText, getOptionTag("true", "true", setting.isArcherAllowed()), true);					
					append(contentText, getOptionTag("false", "false", !setting.isArcherAllowed()), true);	
					append(contentText, getCloseTag("select"), true);					
					appendBR(contentText, 1, true);
					appendClassSettings(contentText, "archer", setting, setting.getArcherSettings());	
					append(contentText, "</span>", true);
					
					// KNIGHT
					append(contentText, "<span id=\"knight\" style=\"display: none;\">", true);
					append(contentText, "<b>Knightsettings:</b><br />", true);
					append(contentText, "Use:", true);
					append(contentText, getSelectTag("knight"), true);
					append(contentText, getOptionTag("true", "true", setting.isKnightAllowed()), true);					
					append(contentText, getOptionTag("false", "false", !setting.isKnightAllowed()), true);	
					append(contentText, getCloseTag("select"), true);					
					appendBR(contentText, 1, true);
					appendClassSettings(contentText, "knight", setting, setting.getKnightSettings());	
					append(contentText, "</span>", true);
					
					// FORM END
					append(contentText, getInputHidden("name", setting.getSettingsName(), 20), true);
					append(contentText, getButton("send", "Save changes"), true);
					append(contentText, getCloseTag("form"), true);
					appendBR(contentText, 2, true);
				}
				else
				{
					contentText.str = "Setting not found!";
				}
			}
		}		
		page.replaceText("%LISTOFSETTINGS%", gameList.str);
		page.replaceText("%TOPIC%", topicText);	
		page.replaceText("%CONTENT%", postText.str + contentText.str);			
	}
	
	public static void appendClassSettings(StringObject contentText, String prefix, CoKGameSettings coksetting, CoKClassSettings setting)
	{
		/** HELMET */
		ArrayList<Material> matList = getAllItemsOf("_helmet");
		append(contentText, "Helmet:", true);
		append(contentText, getSelectTag(prefix + "Helmet"), true);
		append(contentText, getOptionTag("NONE", "-1", setting.isHelmetSet()), true);					
		for(Material mat : matList)
			append(contentText, getOptionTag(mat.name().replace("_" , " "), "" + mat.getId(), setting.isHelmetSetTo(mat.getId())), true);					
		append(contentText, getCloseTag("select"), true);
		appendBR(contentText, 1, true);
		/** CHESTPLATE */
		matList = getAllItemsOf("_chestplate");
		append(contentText, "Chestplate:", true);
		append(contentText, getSelectTag(prefix + "Chestplate"), true);
		append(contentText, getOptionTag("NONE", "-1", setting.isChestPlateSet()), true);					
		for(Material mat : matList)
			append(contentText, getOptionTag(mat.name().replace("_" , " "), "" + mat.getId(), setting.isChestPlateSetTo(mat.getId())), true);					
		append(contentText, getCloseTag("select"), true);
		appendBR(contentText, 1, true);	
		
		/** LEGGINGS */
		matList = getAllItemsOf("_leggings");
		append(contentText, "Leggings:", true);
		append(contentText, getSelectTag(prefix + "Leggings"), true);
		append(contentText, getOptionTag("NONE", "-1", setting.isLeggingsSet()), true);					
		for(Material mat : matList)
			append(contentText, getOptionTag(mat.name().replace("_" , " "), "" + mat.getId(), setting.isLeggingsSetTo(mat.getId())), true);					
		append(contentText, getCloseTag("select"), true);
		appendBR(contentText, 1, true);
		
		/** BOOTS */
		matList = getAllItemsOf("_boots");
		append(contentText, "Boots:", true);
		append(contentText, getSelectTag(prefix + "Boots"), true);
		append(contentText, getOptionTag("NONE", "-1", setting.isBootsSet()), true);					
		for(Material mat : matList)
			append(contentText, getOptionTag(mat.name().replace("_" , " "), "" + mat.getId(), setting.isBootsSetTo(mat.getId())), true);					
		append(contentText, getCloseTag("select"), true);
		appendBR(contentText, 2, true);	
		
		
		/** ITEMS */
		append(contentText, "<b>Items:</b>&nbsp;" + getLink(getImageTag("btn_add.png"), "settingsAddItem.html?name=" + coksetting.getSettingsName() + "&class=" + prefix) + "<br />", true);
		for(int i = 0; i < setting.getItemList().size(); i++)
		{
			CoKItemStack item = setting.getItemList().get(i);
			String txt = item.getAmount() + " x " + Material.getMaterial(item.getId()).name().replace("_" , " ");
			if(item.getSubId() != 0)
				txt += ":" + item.getSubId();
			
			txt += "&nbsp;" + getLink(getImageTag("btn_delete.png"), "settings.html?name=" + coksetting.getSettingsName() + "&class=" + prefix + "&deleteItem=" + i);
						
			append(contentText, txt, true);
			appendBR(contentText, 1, true);	
			item = null;
		}
		appendBR(contentText, 1, true);			
	}
	
	public static ArrayList<Material> getAllItemsOf(String name)
	{
		ArrayList<Material> result = new ArrayList<Material>();
		for(Material mat : Material.values())
		{
			if(mat.name().toLowerCase().contains(name.toLowerCase()))
				result.add(mat);
		}
		return result;
	}
}
