package com.bukkit.gemo.ClashOfKingdoms.web.templates;

import org.bukkit.entity.Player;
import com.bukkit.gemo.ClashOfKingdoms.game.*;
import com.bukkit.gemo.BukkitHTTP.HTTPEvent;
import com.bukkit.gemo.BukkitHTTP.Page;
import com.bukkit.gemo.ClashOfKingdoms.CoKCore;

public class tplIndex extends TemplatePage
{
	public static void execTemplate(Page page, HTTPEvent event)
	{
		StringObject gameList = new StringObject();				
		String topicText 	= "Gameoverview";
		StringObject contentText = new StringObject("Please select a game on the left side or <a href=\"newgame.html\">CREATE A NEW GAME</a>.");
		if(event.getParameter != null)
		{	
			if(event.getParameter.containsKey("name"))
			{			
				String loggedPlayer = event.getCookieParam("Username");
				CoKGame game = CoKCore.GameList.get(event.getParameter.get("name"));
				boolean closingGame = false;
				if(game != null)
				{					
					topicText = "Game: '" + game.gameName + "'";
					clearText(contentText);
					
					if(event.getParameter.containsKey("action"))
					{
						if(event.getParameter.get("action").equalsIgnoreCase("join"))
						{
							joinGame(loggedPlayer, game, contentText);
						}
						else if(event.getParameter.get("action").equalsIgnoreCase("leave"))
						{
							leaveGame(loggedPlayer, game, contentText);
						}
						else if(event.getParameter.get("action").equalsIgnoreCase("punishPlayer") && event.getParameter.containsKey("player"))
						{
							punishPlayer(loggedPlayer, event.getParameter.get("player"), game, contentText);
						}
						else if(event.getParameter.get("action").equalsIgnoreCase("depunishPlayer") && event.getParameter.containsKey("player"))
						{
							depunishPlayer(loggedPlayer, event.getParameter.get("player"), game, contentText);
						}
						else if(event.getParameter.get("action").equalsIgnoreCase("close"))
						{
							closingGame = closeGame(loggedPlayer, game, contentText);
						}
						else if(event.getParameter.get("action").equalsIgnoreCase("start"))
						{
							startGame(loggedPlayer, game, contentText);
						}
						else if(event.getParameter.get("action").equalsIgnoreCase("stop"))
						{
							stopGame(loggedPlayer, game, contentText);
						}
						else if(event.getParameter.get("action").equalsIgnoreCase("showScore"))
						{
							showScore(loggedPlayer, game, contentText);
						}
						else if(event.getParameter.get("action").equalsIgnoreCase("reset"))
						{
							resetGame(loggedPlayer, game, contentText);
						}
					}
					if(event.getParameter.containsKey("joinTeam"))
					{
						joinTeam(loggedPlayer, game, contentText, event.getParameter.get("joinTeam"));
					}
					Player player = CoKCore.getPlayer(loggedPlayer);	
					if(player != null && !closingGame)
					{
						if(game.gameOwner.equalsIgnoreCase(player.getName()))
						{
							append(contentText, getLink(getImageTag("btn_closeGame.png"), "index.html?name=" + game.gameName +"&action=close", "Close game?") + "&nbsp;", true);
						}
							
						if(CoKCore.isPlayerInAnyGame(player))					
						{		
							if(game.isInTeam(player, EnumTeam.REF))
							{
								if(!game.isGameOn())
								{
									append(contentText, getLink(getImageTag("btn_startGame.png"), "index.html?name=" + game.gameName +"&action=start", "Start game?") + "&nbsp;", true);
									append(contentText, getLink(getImageTag("btn_resetGame.png"), "index.html?name=" + game.gameName +"&action=reset", "Reset game?") + "&nbsp;", true);
								}
								else
								{
									append(contentText, getLink(getImageTag("btn_stopGame.png"), "index.html?name=" + game.gameName +"&action=stop", "Stop game?") + "&nbsp;", true);
									append(contentText, getLink(getImageTag("btn_showScore.png"), "index.html?name=" + game.gameName +"&action=showScore") + "&nbsp;", true);
								}
							}
							CoKGame other = CoKCore.getGameByPlayer(player);
							if(other.gameName.equalsIgnoreCase(game.gameName))
							{
								append(contentText, getLink(getImageTag("btn_leaveGame.png"), "index.html?name=" + game.gameName +"&action=leave", "Leave game?"), true);
								appendBR(contentText, 2, true);
							}
							else
							{
								append(contentText, getLink(getImageTag("btn_joinGame.png"), "index.html?name=" + game.gameName +"&action=join", "Join game?"), true);
								appendBR(contentText, 2, true);
							}
						}
						else
						{
							append(contentText, getLink(getImageTag("btn_joinGame.png"), "index.html?name=" + game.gameName +"&action=join", "Join game?"), true);
							appendBR(contentText, 2, true);
						}
					}					
					
					if(!closingGame)
					{
						append(contentText, "<b>Loaded settings:</b> " + game.currentSettings.getSettingsName(), true);
						appendBR(contentText, 2, true);
						
						showTeamButtons(loggedPlayer, game, contentText);
						///////////////////
						// OPEN WRAPPER
						///////////////////
						append(contentText, getOpenTag("div id=\"wrap_team\""), true);
							///////////////////
							// TEAM RED
							///////////////////
							append(contentText, getOpenTag("div id=\"left_team\""), true);
								append(contentText, "<b>Team Red ( " + game.TeamRed.size() + " ) :</b>", true);
								append(contentText, getOpenTag("ul"), true);
								for(String name : game.TeamRed.keySet())
								{
									String text = name;
									if(game.isGameOn())
									{
										if(game.isInTeam(player, EnumTeam.REF))
										{
											if(!game.TeamPun.containsKey(name))
												text += "&nbsp;" + getLink(getImageTag("btn_punishPlayer.png"), "index.html?name=" + game.gameName + "&action=punishPlayer&player=" + name, "Punish '" + name + "'?");
											else
												text += "&nbsp;" + getLink(getImageTag("btn_depunishPlayer.png"), "index.html?name=" + game.gameName + "&action=depunishPlayer&player=" + name, "Depunish '" + name + "'?");
										}
									}
									append(contentText, getListTag(text), true);
								}
								append(contentText, getCloseTag("ul"), true);
							append(contentText, getCloseTag("div"), true);
						
							///////////////////
							// TEAM REF
							///////////////////
							append(contentText, getOpenTag("div id=\"right_team\""), true);
								append(contentText, "<b>Team Ref ( " + game.TeamRef.size() + " ) :</b>", true);
								append(contentText, getOpenTag("ul"), true);
								for(String name : game.TeamRef.keySet())
								{
									append(contentText, getListTag(name), true);
								}
								append(contentText, getCloseTag("ul"), true);
							append(contentText, getCloseTag("div"), true);
							
							///////////////////
							// TEAM BLU
							///////////////////
							append(contentText, getOpenTag("div id=\"middle_team\""), true);
								append(contentText, "<b>Team Blu ( " + game.TeamBlu.size() + " ) :</b>", true);
								append(contentText, getOpenTag("ul"), true);
								for(String name : game.TeamBlu.keySet())
								{
									String text = name;
									if(game.isGameOn())
									{
										if(game.isInTeam(player, EnumTeam.REF))
										{
											if(!game.TeamPun.containsKey(name))
												text += "&nbsp;" + getLink(getImageTag("btn_punishPlayer.png"), "index.html?name=" + game.gameName + "&action=punishPlayer&player=" + name, "Punish '" + name + "'?");
											else
												text += "&nbsp;" + getLink(getImageTag("btn_depunishPlayer.png"), "index.html?name=" + game.gameName + "&action=depunishPlayer&player=" + name, "Depunish '" + name + "'?");
										}
									}
									append(contentText, getListTag(text), true);
								}
								append(contentText, getCloseTag("ul"), true);
							append(contentText, getCloseTag("div"), true);
						///////////////////
						// CLOSE WRAPPER
						///////////////////
						append(contentText, getCloseTag("div"), true);
						append(contentText, "<br style=\"clear:both;\">", true);
					}
				}
				else
				{
					contentText.str = "Game not found!";
				}
			}
		}
		appendGameList(gameList);
		page.replaceText("%LISTOFGAMES%", gameList.str);
		page.replaceText("%TOPIC%", topicText);	
		page.replaceText("%CONTENT%", contentText.str);	
	}
	
	///////////////////////////////
	// RESET GAME
	///////////////////////////////
	public static void resetGame(String refName, CoKGame game, StringObject contentText)
	{
		Player ref = CoKCore.getPlayer(refName);
		if(ref == null)
		{
			append(contentText, "<b>You are is not connected to the server!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}
		if(!game.isInTeam(ref, EnumTeam.REF))
		{
			append(contentText, "<b>You are not a referee!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}	
		
		if(game.isGameOn())
		{
			append(contentText, "<b>Game is currently running!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}
			
		game.resetGame();
		append(contentText, "<b>Game resetted!</b>", true);
		appendBR(contentText, 2, true);
		return;
	}
	
	///////////////////////////////
	// START GAME
	///////////////////////////////
	public static void startGame(String refName, CoKGame game, StringObject contentText)
	{
		Player ref = CoKCore.getPlayer(refName);
		if(ref == null)
		{
			append(contentText, "<b>You are is not connected to the server!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}
		if(!game.isInTeam(ref, EnumTeam.REF))
		{
			append(contentText, "<b>You are not a referee!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}	
		
		if(game.isGameOn())
		{
			append(contentText, "<b>Game is already running!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}
		
		if(game.TeamRed.size() < game.getMinPlayerCount() || game.TeamBlu.size() < game.getMinPlayerCount() )
		{
			append(contentText, "<b>Each team must have at least " + game.getMinPlayerCount() + " members!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}
		
		game.startGame();
		append(contentText, "<b>Game started!</b>", true);
		appendBR(contentText, 2, true);
		return;
	}
	
	///////////////////////////////
	// STOP GAME
	///////////////////////////////
	public static void stopGame(String refName, CoKGame game, StringObject contentText)
	{
		Player ref = CoKCore.getPlayer(refName);
		if(ref == null)
		{
			append(contentText, "<b>You are is not connected to the server!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}
		if(!game.isInTeam(ref, EnumTeam.REF))
		{
			append(contentText, "<b>You are not a referee!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}	
		
		if(!game.isGameOn())
		{
			append(contentText, "<b>Game is not running!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}
			
		game.stopGame();
		append(contentText, "<b>Game stopped!</b>", true);
		appendBR(contentText, 2, true);
		return;
	}
	
	///////////////////////////////
	// SHOW SCORE
	///////////////////////////////
	public static void showScore(String refName, CoKGame game, StringObject contentText)
	{
		Player ref = CoKCore.getPlayer(refName);
		if(ref == null)
		{
			append(contentText, "<b>You are is not connected to the server!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}
		if(!game.isInTeam(ref, EnumTeam.REF))
		{
			append(contentText, "<b>You are not a referee!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}	
		
		if(!game.isGameOn())
		{
			append(contentText, "<b>Game is not running!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}
			
		game.showScore();
		append(contentText, "<b>Score broadcasted!</b>", true);
		appendBR(contentText, 2, true);
		return;
	}	
	
	///////////////////////////////
	// CLOSE GAME
	///////////////////////////////
	public static boolean closeGame(String playername, CoKGame game, StringObject contentText)
	{
		if(!game.gameOwner.equalsIgnoreCase(playername))
		{
			append(contentText, "<b>You are not the gameowner!</b>", true);
			appendBR(contentText, 2, true);
			return false;
		}
		game.closeGame();
		append(contentText, "<b>Game closed</b>", true);
		return true;
	}
	
	///////////////////////////////
	// DEPUNISH PLAYER
	///////////////////////////////
	public static void depunishPlayer(String refName, String playername, CoKGame game, StringObject contentText)
	{
		Player ref = CoKCore.getPlayer(refName);
		if(ref == null)
		{
			append(contentText, "<b>Could not depunish '" + playername +"'. (You are is not connected to the server)!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}
		if(!game.isInTeam(ref, EnumTeam.REF))
		{
			append(contentText, "<b>You are not a referee!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}	
		
		if(!game.isGameOn())
		{
			append(contentText, "<b>Game is not running!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}
		
		Player player = CoKCore.getPlayer(playername);
		if(player == null)
		{
			append(contentText, "<b>Could not depunish '" + playername +"' (User is not connected to the server)!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}
		if(!CoKCore.isPlayerInGame(player, game))
		{
			append(contentText, "<b>Could not depunish '" + playername +"' (Not in this game)!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}
		if(!game.TeamPun.containsKey(playername))
		{
			append(contentText, "<b>'" + playername +"' is not in jail!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}
		game.depunishPlayer(player);
		append(contentText, "<b>Player '" + playername +"' is now free!</b>", true);
		appendBR(contentText, 2, true);
		return;
	}
	
	///////////////////////////////
	// PUNISH PLAYER
	///////////////////////////////	
	public static void punishPlayer(String refName, String playername, CoKGame game, StringObject contentText)
	{
		Player ref = CoKCore.getPlayer(refName);
		if(ref == null)
		{
			append(contentText, "<b>Could not punish '" + playername +"'. (You are is not connected to the server)!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}
		if(!game.isInTeam(ref, EnumTeam.REF))
		{
			append(contentText, "<b>You are not a referee!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}	
		
		if(!game.isGameOn())
		{
			append(contentText, "<b>Game is not running!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}
		
		Player player = CoKCore.getPlayer(playername);
		if(player == null)
		{
			append(contentText, "<b>Could not punish '" + playername +"' (User is not connected to the server)!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}
		if(!CoKCore.isPlayerInGame(player, game))
		{
			append(contentText, "<b>Could not punish '" + playername +"' (Not in this game)!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}
		if(game.TeamPun.containsKey(playername))
		{
			append(contentText, "<b>'" + playername +"' is already in jail!</b>", true);
			appendBR(contentText, 2, true);
			return;
		}
		game.punishPlayer(player);
		append(contentText, "<b>Player '" + playername +"' is now in jail!</b>", true);
		appendBR(contentText, 2, true);
		return;
	}
	
	///////////////////////////////
	// SHOW TEAM BUTTON
	///////////////////////////////	
	public static void showTeamButtons(String playername, CoKGame game, StringObject contentText)
	{
		Player player = CoKCore.getPlayer(playername);
		if(player != null && CoKCore.isPlayerInGame(player, game))
		{
			if(!game.isInTeam(player, EnumTeam.RED))
				append(contentText, getLink(getImageTag("btn_joinRed.png") + "&nbsp;", "index.html?name=" + game.gameName +"&joinTeam=RED"), true);
			if(!game.isInTeam(player, EnumTeam.BLU))
				append(contentText, getLink(getImageTag("btn_joinBlu.png") + "&nbsp;", "index.html?name=" + game.gameName +"&joinTeam=BLU"), true);
			if(!game.isInTeam(player, EnumTeam.REF))
				append(contentText, getLink(getImageTag("btn_joinRef.png") + "&nbsp;", "index.html?name=" + game.gameName +"&joinTeam=REF"), true);
			appendBR(contentText, 2, true);
		}
	}

	///////////////////////////////
	// JOIN TEAM
	///////////////////////////////
	public static void joinTeam(String playername, CoKGame game, StringObject contentText, String Team)
	{
		Player player = CoKCore.getPlayer(playername);
		if(player == null)
		{
			append(contentText, "<b>Could not switch the team. (You are not connected to the server)!</b>", true);
			appendBR(contentText, 2, true);
		}
		else
		{
			if(CoKCore.isPlayerInGame(player, game))
			{
				if(Team == null)
					Team = "NONE";
				
				if(Team.equalsIgnoreCase("RED"))
				{
					game.joinTeam(player, EnumTeam.RED);
				}
				else if(Team.equalsIgnoreCase("BLU"))
				{
					game.joinTeam(player, EnumTeam.BLU);
				}
				else if(Team.equalsIgnoreCase("REF"))
				{
					game.joinTeam(player, EnumTeam.REF);
				}
				else
				{
					game.joinTeam(player, EnumTeam.NONE);
				}
				append(contentText, "<b>You switched the team!</b>", true);
				appendBR(contentText, 2, true);
			}
			else
			{
				append(contentText, "<b>You are not in this game!</b>", true);
				appendBR(contentText, 2, true);
			}	
		}
	}
	
	///////////////////////////////
	// JOIN GAME
	///////////////////////////////	
	public static void joinGame(String playername, CoKGame game, StringObject contentText)
	{
		Player player = CoKCore.getPlayer(playername);
		if(player == null)
		{
			append(contentText, "<b>Could not join the game (You are not connected to the server)!</b>", true);
			appendBR(contentText, 2, true);
		}
		else
		{
			if(CoKCore.isPlayerInAnyGame(player))
			{
				CoKGame other = CoKCore.getGameByPlayer(player);
				if(!other.gameName.equalsIgnoreCase(game.gameName))
				{
					other.leaveGame(player);
					game.joinGame(player);
				}
			}
			else
			{
				game.joinGame(player);
			}
			append(contentText, "<b>You joined this game!</b>", true);
			appendBR(contentText, 2, true);
		}
	}
	
	///////////////////////////////
	// LEAVE GAME
	///////////////////////////////	
	public static void leaveGame(String playername, CoKGame game, StringObject contentText)
	{
		Player player = CoKCore.getPlayer(playername);
		if(player == null)
		{
			append(contentText, "<b>Could not leave the game (You are not connected to the server)!</b>", true);
			appendBR(contentText, 2, true);
		}
		else
		{
			if(CoKCore.isPlayerInGame(player, game))
			{
				game.leaveGame(player);
				append(contentText, "<b>You left this game!</b>", true);
				appendBR(contentText, 2, true);
			}
			else
			{
				append(contentText, "<b>You are not in this game!</b>", true);
				appendBR(contentText, 2, true);
			}			
		}
	}
}
