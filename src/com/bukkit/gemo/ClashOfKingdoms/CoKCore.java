package com.bukkit.gemo.ClashOfKingdoms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.alta189.sqlLibrary.MySQL.mysqlCore;
import com.bukkit.gemo.BukkitHTTP.HTTPCore;
import com.bukkit.gemo.BukkitHTTP.HTTPPlugin;
import com.bukkit.gemo.ClashOfKingdoms.Listener.*;
import com.bukkit.gemo.ClashOfKingdoms.commands.all.*;
import com.bukkit.gemo.ClashOfKingdoms.commands.ref.*;
import com.bukkit.gemo.ClashOfKingdoms.web.CoKHTTP;
import com.bukkit.gemo.ClashOfKingdoms.game.*;

public class CoKCore extends JavaPlugin {
    private static String pluginName = "ClashOfKingdoms 0.75 by GeMo";
    public static Server server;

    public static CoKBL blockListener;
    public static CoKEL entityListener;
    public static CoKPL playerListener;
    public static HashMap<String, CoKGameSettings> allSettings;
    public static HashMap<String, CoKGame> GameList;

    // WEBSERVER
    public static HTTPPlugin thisHTTP;

    // MY SQL
    public static mysqlCore mySQL;
    public boolean isEnabled = false;
    public String dbHost = "localhost";
    public String dbUser = "minestar_contao";
    public String dbPass = "lur4pqna";
    public String dbDatabase = "minestar_contao";

    // AUSGABE IN DER CONSOLE
    public static void printInConsole(String str) {
        System.out.println("[Clash of Kingdoms] " + str);
    }

    // ON DISABLE
    @Override
    public void onDisable() {
        System.out.println(pluginName + " disabled");
    }

    // LOAD GAME SETTINGS
    public boolean loadGameSettings() {
        allSettings = new HashMap<String, CoKGameSettings>();
        File f = new File("plugins/ClashOfKingdoms/GameSettings.db");
        if (!f.exists()) {
            printInConsole("No GameSettings loaded.");
            return false;
        }

        try {
            ObjectInputStream objIn2 = new ObjectInputStream(new BufferedInputStream(new FileInputStream("plugins/ClashOfKingdoms/GameSettings.db")));
            // Area newArea;
            int count = (Integer) objIn2.readObject();
            int i = 0;
            while (i < count) {
                CoKGameSettings newSettings = (CoKGameSettings) objIn2.readObject();
                allSettings.put(newSettings.getSettingsName(), newSettings);
                i++;
            }
            objIn2.close();
            printInConsole(allSettings.size() + " GameSettings loaded.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            printInConsole("Error while reading plugins/ClashOfKingdoms/GameSettings.db");
            return false;
        }
    }

    // SAVE SETTINGS
    public static void saveGameSettings() {
        // AREAS SPEICHERN
        File folder = new File("plugins/ClashOfKingdoms");
        folder.mkdirs();

        if (new File("plugins/ClashOfKingdoms/GameSettings.db").exists()) {
            new File("plugins/ClashOfKingdoms/GameSettings.db").delete();
        }

        try {
            ObjectOutputStream objOut2 = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("plugins/ClashOfKingdoms/GameSettings.db")));
            objOut2.writeObject(CoKCore.allSettings.values().size());
            for (CoKGameSettings thisSetting : CoKCore.allSettings.values()) {
                objOut2.writeObject(thisSetting);
            }
            objOut2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ON ENABLE
    @Override
    public void onEnable() {
        loadGameSettings();

        CoKCore.server = getServer();
        GameList = new HashMap<String, CoKGame>();

        blockListener = new CoKBL(this);
        entityListener = new CoKEL(this);
        playerListener = new CoKPL(this);
        PluginManager pm = getServer().getPluginManager();

        // BLOCK LISTENER
        pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Highest, this);
        pm.registerEvent(Event.Type.BLOCK_BURN, blockListener, Event.Priority.Highest, this);
        pm.registerEvent(Event.Type.BLOCK_FADE, blockListener, Event.Priority.Highest, this);
        pm.registerEvent(Event.Type.BLOCK_FORM, blockListener, Event.Priority.Highest, this);
        pm.registerEvent(Event.Type.BLOCK_IGNITE, blockListener, Event.Priority.Highest, this);
        pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Event.Priority.Highest, this);
        pm.registerEvent(Event.Type.BLOCK_SPREAD, blockListener, Event.Priority.Highest, this);
        pm.registerEvent(Event.Type.LEAVES_DECAY, blockListener, Event.Priority.Highest, this);

        // ENTITY LISTENER
        pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Event.Priority.Highest, this);
        pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener, Event.Priority.Highest, this);
        pm.registerEvent(Event.Type.ENTITY_EXPLODE, entityListener, Event.Priority.Highest, this);

        // PLAYER LISTENER
        pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Event.Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_DROP_ITEM, playerListener, Event.Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_PICKUP_ITEM, playerListener, Event.Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Highest, this);
        pm.registerEvent(Event.Type.PLAYER_RESPAWN, playerListener, Event.Priority.Highest, this);

        registerHTTP();

        System.out.println(pluginName + " enabled");
    }

    // REGISTER AT HTTP
    public void registerHTTP() {
        Plugin httpPlugin = server.getPluginManager().getPlugin("BukkitHTTP");
        if (httpPlugin != null) {
            if (!httpPlugin.isEnabled()) {
                server.getPluginManager().enablePlugin(httpPlugin);
            }
            HTTPCore http = (HTTPCore) httpPlugin;
            thisHTTP = new CoKHTTP("cok", "Clash of Kingdoms", "ClashOfKingdoms/web", true);
            thisHTTP.setOwn404Page(true);
            http.registerPlugin(thisHTTP);
            isEnabled = initMySQL();
        } else {
            printInConsole("BukkitHTTP not found!");
        }
    }

    // GET PLAYER
    public static Player getPlayer(String name) {
        Player[] pList = server.getOnlinePlayers();
        for (Player player : pList) {
            if (player.getName().equalsIgnoreCase(name))
                return player;
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;

        if (!label.equalsIgnoreCase("cok"))
            return true;

        if (args == null)
            return true;

        Player player = (Player) sender;
        if (args.length == 0) {
            String[] newArgs = new String[1];
            newArgs[0] = "help";
            cmdHelp.ExecuteCommand(player, newArgs);
            return true;
        }

        if (args[0].equalsIgnoreCase("showscore")) {
            cmdShowScore.ExecuteCommand(player, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("start")) {
            cmdStart.ExecuteCommand(player, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("pause")) {
            cmdPause.ExecuteCommand(player, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("stop")) {
            cmdStop.ExecuteCommand(player, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("reset")) {
            cmdReset.ExecuteCommand(player, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("list")) {
            cmdList.ExecuteCommand(player, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("quit")) {
            cmdQuit.ExecuteCommand(player, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("red") || args[0].equalsIgnoreCase("blu") || args[0].equalsIgnoreCase("ref") || args[0].equalsIgnoreCase("none")) {
            cmdSwitchTeam.ExecuteCommand(player, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("base")) {
            cmdBase.ExecuteCommand(player, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("spawn")) {
            cmdSpawn.ExecuteCommand(player, args);

            return true;
        }
        if (args[0].equalsIgnoreCase("create")) {
            cmdCreate.ExecuteCommand(player, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("join")) {
            cmdJoin.ExecuteCommand(player, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("close")) {
            cmdClose.ExecuteCommand(player, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("punish")) {
            cmdPunish.ExecuteCommand(player, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("depunish")) {
            cmdDepunish.ExecuteCommand(player, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("punishteam")) {
            cmdPunishTeam.ExecuteCommand(player, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("savearea")) {
            cmdAreaSave.ExecuteCommand(player, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("resetarea")) {
            cmdAreaReset.ExecuteCommand(player, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("settings")) {
            if (args[1].equalsIgnoreCase("list")) {
                cmdSettingsList.ExecuteCommand(player, args);
                return true;
            }
            if (args[1].equalsIgnoreCase("save")) {
                cmdSettingsSave.ExecuteCommand(player, args);
                return true;
            }
            if (args[1].equalsIgnoreCase("create")) {
                cmdSettingsCreate.ExecuteCommand(player, args);
                return true;
            }
            if (args[1].equalsIgnoreCase("load")) {
                cmdSettingsLoad.ExecuteCommand(player, args);
                return true;
            }
            if (args[1].equalsIgnoreCase("delete")) {
                cmdSettingsDelete.ExecuteCommand(player, args);
                return true;
            }
            if (args[1].equalsIgnoreCase("height")) {
                cmdSettingsHeight.ExecuteCommand(player, args);
                return true;
            }
            if (args[1].equalsIgnoreCase("material")) {
                cmdSettingsMaterial.ExecuteCommand(player, args);
                return true;
            }
        }
        if (args[0].equalsIgnoreCase("move")) {
            cmdMovePlayer.ExecuteCommand(player, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("newLeader")) {
            cmdRefreshLeader.ExecuteCommand(player, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("newArcher")) {
            cmdRefreshArcher.ExecuteCommand(player, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("newKnight")) {
            cmdRefreshKnight.ExecuteCommand(player, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("teams")) {
            cmdListTeams.ExecuteCommand(player, args);
            return true;
        }

        String[] newArgs = new String[1];
        newArgs[0] = "help";
        cmdHelp.ExecuteCommand(player, newArgs);
        return true;
    }

    // IS PLAYER IN ANY GAME
    public static boolean isPlayerInAnyGame(Player player) {
        Iterator<CoKGame> iterator = GameList.values().iterator();
        CoKGame game = null;
        while (iterator.hasNext()) {
            game = iterator.next();
            if (isPlayerInGame(player, game)) {
                return true;
            }
        }
        game = null;
        return false;
    }

    // IS PLAYER IN SPECIFIC GAME
    public static boolean isPlayerInGame(Player player, CoKGame game) {
        return game.isInGame(player);
    }

    // GET GAME BY PLAYER
    public static CoKGame getGameByPlayer(Player player) {
        Iterator<CoKGame> iterator = GameList.values().iterator();
        CoKGame game = null;
        while (iterator.hasNext()) {
            game = iterator.next();
            if (isPlayerInGame(player, game)) {
                return game;
            }
        }
        game = null;
        return null;
    }

    // SETTINGS IN USE
    public static boolean settingsInUse(String name) {
        Iterator<CoKGame> iterator = GameList.values().iterator();
        CoKGame game = null;
        while (iterator.hasNext()) {
            game = iterator.next();
            if (game.currentSettings.getSettingsName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        game = null;
        return false;
    }

    // SETTINGS IN USE
    public static boolean settingsInUseRunning(String name) {
        Iterator<CoKGame> iterator = GameList.values().iterator();
        CoKGame game = null;
        while (iterator.hasNext()) {
            game = iterator.next();
            if (game.isGameOn() && game.currentSettings.getSettingsName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        game = null;
        return false;
    }

    // /////////////////////
    //
    // INIT MYSQL
    //
    // /////////////////////
    public boolean initMySQL() {
        try {
            mySQL = new mysqlCore(Logger.getLogger("Minecraft"), "ClashOfKingdoms", this.dbHost, this.dbDatabase, this.dbUser, this.dbPass);
            mySQL.initialize();
            if (mySQL.checkConnection()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // /////////////////////
    //
    // GET CONTAO ID
    //
    // /////////////////////
    public static int getContaoID(String username) {
        String query = "SELECT `id` FROM `tl_member` WHERE `username`='" + username + "'";
        ResultSet result = null;
        try {
            result = mySQL.sqlQuery(query);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            while (result != null && result.next()) {
                return result.getInt("id");
            }
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // /////////////////////
    //
    // GET MC-NAME FROM DB
    //
    // /////////////////////
    public static String getIngameNick(String WebNick) {
        int ID = getContaoID(WebNick);
        String query = "Select `minecraft_nick` FROM `mc_pay` WHERE `contao_user_id`='" + ID + "' LIMIT 1";
        ResultSet result = null;
        try {
            result = mySQL.sqlQuery(query);
            while (result != null && result.next()) {
                return result.getString("minecraft_nick");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
