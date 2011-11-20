package com.bukkit.gemo.ClashOfKingdoms.web;

import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.bukkit.gemo.BukkitHTTP.HTTPEvent;
import com.bukkit.gemo.BukkitHTTP.Page;
import com.bukkit.gemo.BukkitHTTP.HTTPPlugin;
import com.bukkit.gemo.ClashOfKingdoms.CoKCore;
import com.bukkit.gemo.ClashOfKingdoms.web.templates.StringObject;
import com.bukkit.gemo.ClashOfKingdoms.web.templates.TemplatePage;

public class CoKHTTP extends HTTPPlugin
{
	///////////////////////
	//
	// CONSTRUCTOR
	//
	///////////////////////	
	public CoKHTTP(String rootAlias, String pluginName, String root, boolean useAuth) 
	{
		super(rootAlias, pluginName, root, useAuth);		
	}

	///////////////////////
	//
	// HANDLE GET REQUEST
	//
	///////////////////////		
	@Override
	public void handleGetRequest(Page page, HTTPEvent event)
	{
		TemplateFinder.findTemplate(page, event);
	}
	
	///////////////////////
	//
	// HANDLE POST REQUEST
	//
	///////////////////////		
	@Override
	public void handlePostRequest(Page page, HTTPEvent event) 
	{
		TemplateFinder.findTemplate(page, event);
	}

	///////////////////////
	//
	// HANDLE 404 PAGE
	//
	///////////////////////	
	@Override
	public void handle404Page(Page page, HTTPEvent event)
	{
		TemplateFinder.findTemplate(page, event);
	}
	
	///////////////////////
	//
	// LOGIN SUCCESSFUL
	//
	///////////////////////	
	@Override
	public String loginSuccessful(HTTPEvent event)
	{
		try
		{
			HashMap<String, String> param = event.postParameter;
			if(param == null)
				return null;	
			if(param.size() < 3)
				return null;	
			
			String pw = getUserPassword(param.get("username"));		
			if(pw.split(":")[0] == null || pw.split(":")[1] == null)
				return null;		
					
			if(!Crypt.SHA1(pw.split(":")[1] + param.get("password")).contentEquals(pw.split(":")[0]))
				return null;
			
			String username = CoKCore.getIngameNick(param.get("username"));
			if(username == null)
				return null;	
			
			return "Username=" + username;
		}
		catch(Exception e)
		{
			return null;
		}
	}	
	
	///////////////////////
	//
	// LOGIN FAILED
	//
	///////////////////////	
	@Override
	public void handleWrongLogin(Page page, HTTPEvent event)
	{
		StringObject gameList = new StringObject();		
		TemplatePage.appendGameList(gameList);		
		page.replaceText("%LISTOFGAMES%", gameList.str);
		page.replaceText("%MESSAGE%", "<font color=\"red\"><b>Login failed!</b></font><br /><br />");
	}

	///////////////////////
	//
	// GET CONTAO PASSWORD
	//
	///////////////////////
	public static String getUserPassword(String username)
	{
		String query = "SELECT `password` FROM `tl_member` WHERE `username`='" + username + "'";
		ResultSet result = null;
		try {
			result = CoKCore.mySQL.sqlQuery(query);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		try 
		{
			while (result != null && result.next())
			{
				return result.getString("password");				
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
}
