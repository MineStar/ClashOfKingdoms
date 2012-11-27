package de.minestar.clashofkingdoms.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;

import de.minestar.clashofkingdoms.COKCore;
import de.minestar.clashofkingdoms.classes.EnumPlayerClass;
import de.minestar.clashofkingdoms.classes.PlayerClass;
import de.minestar.clashofkingdoms.enums.EnumTeam;
import de.minestar.clashofkingdoms.enums.GameState;
import de.minestar.clashofkingdoms.manager.GameManager;
import de.minestar.clashofkingdoms.utils.BlockVector;

public class COKGame {

    private static final String GAME_JOIN = "'%s' has joined the game ( %s ) !";
    private static final String GAME_QUIT = "'%s' has left the game!";
    private static final String TEAM_SWITCH = "'%s' is now in %s!";
    private static final String TEAM_TOO_FEW = "%s has too few players!";
    private static final String PLAYER_CLASS_NEW = "'%s' is the new %s of %s!";
    private static final String PLAYER_CLASS_KILL = "The %s of %s has been killed!";
    private static final String GAME_WIN = ChatColor.GOLD + "[COK] %s has won the game!";

    private static final int MIN_PLAYERS_PER_TEAM = 1;

    private GameManager gameManager;

    private final String gameName;
    private HashMap<String, COKPlayer> playerList;
    private GameState gameState = GameState.STOPPED;
    private GameSettings settings;

    // TEAMDATA
    private HashMap<EnumTeam, TeamData> teamData;

    public COKGame(String gameName) {
        this.gameManager = COKCore.gameManager;
        this.gameName = gameName;
        this.playerList = new HashMap<String, COKPlayer>();

        // TeamData
        this.teamData = new HashMap<EnumTeam, TeamData>();
        this.addTeamData(EnumTeam.BLU);
        this.addTeamData(EnumTeam.RED);
        this.addTeamData(EnumTeam.SPEC);
        this.addTeamData(EnumTeam.REF);

        // settings
        this.settings = new GameSettings();
    }

    private void addTeamData(EnumTeam team) {
        if (this.teamData.containsKey(team)) {
            throw new RuntimeException("TeamData '" + team.name() + "' already exists!");
        }
        this.teamData.put(team, new TeamData(team));
    }

    // ///////////////////////////////////////////////////////////////
    //
    // Playerrelated stuff
    //
    // ///////////////////////////////////////////////////////////////

    public COKPlayer getPlayer(String playerName) {
        return this.playerList.get(playerName);
    }

    public boolean isPlayerInGame(String playerName) {
        return (this.getPlayer(playerName) != null);
    }

    public boolean switchTeam(String playerName, EnumTeam newTeam) {
        COKPlayer player = this.getPlayer(playerName);
        if (player != null) {
            this.teamData.get(player.getTeam()).removePlayer(player);
            this.teamData.get(newTeam).addPlayer(player);
            player.setTeam(newTeam);
            if (player.isInTeam(EnumTeam.RED) || player.isInTeam(EnumTeam.BLU)) {
                player.clearInventory();
            }

            if (!this.isStopped()) {
                this.checkPlayerClasses(EnumTeam.RED);
                this.checkPlayerClasses(EnumTeam.BLU);
            }

            if (this.isStopped() && !newTeam.equals(EnumTeam.REF)) {
                if (this.teamData.get(EnumTeam.SPEC).getSpawn() != null) {
                    player.getBukkitPlayer().teleport(this.teamData.get(EnumTeam.SPEC).getSpawn());
                }
            } else {
                if (this.teamData.get(newTeam).getSpawn() != null) {
                    player.getBukkitPlayer().teleport(this.teamData.get(newTeam).getSpawn());
                } else if (this.teamData.get(EnumTeam.SPEC).getSpawn() != null) {
                    player.getBukkitPlayer().teleport(this.teamData.get(EnumTeam.SPEC).getSpawn());
                }
            }

            this.sendMessageToAll(ChatColor.GRAY, String.format(TEAM_SWITCH, playerName, newTeam.getFullTeamName(ChatColor.GRAY)));
            return true;
        }
        return false;
    }

    public void onPlayerDisconnect(String playerName) {
        this.playerQuitGame(playerName);
    }

    public void onPlayerDeath(String playerName) {
        // interact with classes
        COKPlayer player = this.getPlayer(playerName);
        if (player.getPlayerClass() != null) {
            String oldClass = player.getPlayerClass().getClassName();
            int punishBlocks = (int) Math.ceil(this.getTeamData(player.getTeam()).getBaseBlockCount() * player.getPlayerClass().getPunishMultiplicator());
            this.punish(punishBlocks, player.getTeam(), false);
            this.sendMessageToAll(ChatColor.WHITE, String.format(PLAYER_CLASS_KILL, this.settings.getPlayerClass(oldClass).getClassName(), player.getTeam().getFullTeamName(ChatColor.WHITE)));
        }
    }

    public void getRandomizedPlayerClass(EnumTeam team, PlayerClass playerClass) {
        // only team red & team blu
        if (!team.equals(EnumTeam.RED) && !team.equals(EnumTeam.BLU)) {
            return;
        }

        // playerclass must be enabled
        if (!playerClass.isEnabled()) {
            return;
        }

        // get free players
        ArrayList<COKPlayer> freePlayers = new ArrayList<COKPlayer>();
        for (COKPlayer player : this.getTeamData(team).getPlayerList().values()) {
            if (player.getPlayerClass() == null && player.getBukkitPlayer().isOnline() && !player.getBukkitPlayer().isDead()) {
                freePlayers.add(player);
            }
        }

        // find a randomized player
        if (freePlayers.size() > 0) {
            Random random = new Random();
            int index = random.nextInt(freePlayers.size());
            COKPlayer player = freePlayers.get(index);
            player.setPlayerClass(playerClass);
            playerClass.giveItems(player.getBukkitPlayer());
            this.sendMessageToAll(ChatColor.GRAY, String.format(PLAYER_CLASS_NEW, player.getPlayerName(), playerClass.getClassName(), team.getFullTeamName(ChatColor.GRAY)));
        }
    }

    public boolean playerJoinGame(String playerName, EnumTeam team) {
        COKPlayer player = new COKPlayer(playerName, this);

        // handle automatic balance
        if (team == null) {
            int redTeamSize = this.getTeamData(EnumTeam.RED).getPlayerCount();
            int bluTeamSize = this.getTeamData(EnumTeam.BLU).getPlayerCount();
            if (redTeamSize <= bluTeamSize) {
                team = EnumTeam.RED;
            } else {
                team = EnumTeam.BLU;
            }
        }

        if (this.gameManager.addToPlayerList(player)) {
            player.setTeam(team);
            this.playerList.put(player.getPlayerName(), player);
            this.teamData.get(team).addPlayer(player);
            this.sendMessageToAll(ChatColor.GRAY, String.format(GAME_JOIN, playerName, team.getFullTeamName(ChatColor.GRAY)));

            if (!this.isStopped()) {
                this.checkPlayerClasses(EnumTeam.RED);
                this.checkPlayerClasses(EnumTeam.BLU);
            }

            if (this.isStopped() && !team.equals(EnumTeam.REF)) {
                if (this.teamData.get(EnumTeam.SPEC).getSpawn() != null) {
                    player.getBukkitPlayer().teleport(this.teamData.get(EnumTeam.SPEC).getSpawn());
                }
            } else {
                if (this.teamData.get(team).getSpawn() != null) {
                    player.getBukkitPlayer().teleport(this.teamData.get(team).getSpawn());
                } else if (this.teamData.get(EnumTeam.SPEC).getSpawn() != null) {
                    player.getBukkitPlayer().teleport(this.teamData.get(EnumTeam.SPEC).getSpawn());
                }
            }
            return true;
        }
        return false;
    }

    public boolean playerQuitGame(String playerName) {
        COKPlayer player = this.getPlayer(playerName);
        if (player != null && this.gameManager.removeFromPlayerList(player)) {
            // teleport to SPEC spawn
            if (this.teamData.get(EnumTeam.SPEC).getSpawn() != null) {
                player.getBukkitPlayer().teleport(this.teamData.get(EnumTeam.SPEC).getSpawn());
            }

            // get the new playerclass, if there was one
            if (player.isInTeam(EnumTeam.RED) || player.isInTeam(EnumTeam.BLU)) {
                if (!this.isStopped()) {
                    PlayerClass clazz = player.getPlayerClass();
                    if (clazz != null) {
                        String oldClass = clazz.getClassName();
                        int punishBlocks = (int) Math.ceil((this.getTeamData(player.getTeam()).getBaseBlockCount() * clazz.getPunishMultiplicator()));
                        this.punish(punishBlocks, player.getTeam(), false);
                        this.getRandomizedPlayerClass(player.getTeam(), this.settings.getPlayerClass(oldClass));
                        player.setPlayerClass(null);
                    }
                }
            }

            // clear inventory and remove the player
            player.clearInventory();
            this.teamData.get(player.getTeam()).removePlayer(player);
            this.playerList.remove(player.getPlayerName());

            // show the player
            this.showPlayer(player);

            // send info
            this.sendMessageToAll(ChatColor.GRAY, String.format(GAME_QUIT, playerName));

            // pause the game, if there are too few players
            if (this.teamData.get(player.getTeam()).getPlayerCount() < MIN_PLAYERS_PER_TEAM) {
                this.sendMessageToAll(ChatColor.RED, String.format(TEAM_TOO_FEW, player.getTeam().getFullTeamName(ChatColor.RED)));
                this.pauseGame();
            }

            return true;
        }
        return false;
    }

    public boolean checkForWinner(EnumTeam team) {
        if (this.isBaseComplete(team)) {
            if (team.equals(EnumTeam.RED)) {
                this.sendMessageToAll(String.format(GAME_WIN, EnumTeam.BLU.getFullTeamName(ChatColor.GOLD)));
                this.endGame();
                return true;
            } else if (team.equals(EnumTeam.BLU)) {
                this.sendMessageToAll(String.format(GAME_WIN, EnumTeam.RED.getFullTeamName(ChatColor.GOLD)));
                this.endGame();
                return true;
            }
        }
        return false;
    }

    // ///////////////////////////////////////////////////////////////
    //
    // Messagerelated stuff
    //
    // ///////////////////////////////////////////////////////////////

    public void sendMessageToTeam(String message, EnumTeam team) {
        this.getTeamData(team).sendMessageToAll(message);
    }

    public void sendMessageToTeam(ChatColor color, String message, EnumTeam team) {
        this.getTeamData(team).sendMessageToAll(color, message);
    }

    public void sendMessageToAll(String message) {
        for (COKPlayer player : this.playerList.values()) {
            player.sendMessage(message);
        }
    }

    public void sendMessageToAll(ChatColor color, String message) {
        this.sendMessageToAll(color + message);
    }

    // ///////////////////////////////////////////////////////////////
    //
    // Gamerelated stuff
    //
    // ///////////////////////////////////////////////////////////////

    /**
     * Start the game
     */
    public void startGame() {

        // TODO: enable check
        // // CHECK PLAYERCOUNT
        // if (this.teamData.get(EnumTeam.RED).getPlayerCount() < MIN_PLAYERS_PER_TEAM) {
        // this.sendMessageToAll(ChatColor.RED, String.format(TEAM_TOO_FEW, EnumTeam.RED.getFullTeamName(ChatColor.RED)));
        // return;
        // }
        //
        // // CHECK PLAYERCOUNT
        // if (this.teamData.get(EnumTeam.BLU).getPlayerCount() < MIN_PLAYERS_PER_TEAM) {
        // this.sendMessageToAll(ChatColor.RED, String.format(TEAM_TOO_FEW, EnumTeam.BLU.getFullTeamName(ChatColor.RED)));
        // return;
        // }

        this.resetGame();

        // tp all to spawn
        for (COKPlayer player : this.playerList.values()) {
            Location location = this.getTeamData(player.getTeam()).getSpawn();
            if (location == null) {
                continue;
            }

            player.getBukkitPlayer().teleport(location);
            player.getBukkitPlayer().setGameMode(GameMode.SURVIVAL);
        }

        // get new classes
        for (PlayerClass playerClass : this.settings.getPlayerClassList()) {
            if (playerClass.getClassName().equalsIgnoreCase(EnumPlayerClass.REFEREE.getClassName())) {
                continue;
            }

            if (!playerClass.isEnabled()) {
                continue;
            }

            this.getRandomizedPlayerClass(EnumTeam.RED, playerClass);
            this.getRandomizedPlayerClass(EnumTeam.BLU, playerClass);
        }

        // add items for referees
        for (COKPlayer player : this.getTeamData(EnumTeam.REF).getPlayerList().values()) {
            player.setPlayerClass(this.settings.getPlayerClass(EnumPlayerClass.REFEREE.getClassName()));
            this.settings.getPlayerClass(EnumPlayerClass.REFEREE.getClassName()).giveItems(player.getBukkitPlayer());
        }

        this.setGameState(GameState.RUNNING);
        this.sendMessageToAll(ChatColor.GREEN, "The game has started!");
    }

    /**
     * Stop the game
     */
    public void stopGame() {
        if (!this.isStopped()) {
            this.sendMessageToAll(ChatColor.GREEN, "The game has been stopped by an Admin!");
        }
        this.setGameState(GameState.STOPPED);
        this.resetGame();
    }

    /**
     * End the game
     */
    public void endGame() {
        this.setGameState(GameState.STOPPED);
    }

    /**
     * Reset the game
     */
    private void resetGame() {
        this.teamData.get(EnumTeam.RED).resetBase(this.settings.getBaseHeight());
        this.teamData.get(EnumTeam.BLU).resetBase(this.settings.getBaseHeight());
        for (COKPlayer player : this.playerList.values()) {
            if (player.isInTeam(EnumTeam.RED) || player.isInTeam(EnumTeam.BLU)) {
                player.clearInventory();
                player.getBukkitPlayer().setHealth(20);
                player.getBukkitPlayer().setFoodLevel(20);
            }
            player.setPlayerClass(null);
        }
    }

    public void checkPlayerClasses(EnumTeam team) {
        if (!team.equals(EnumTeam.RED) && !team.equals(EnumTeam.BLU)) {
            return;
        }

        for (EnumPlayerClass clazz : EnumPlayerClass.values()) {
            if (clazz.equals(EnumPlayerClass.REFEREE)) {
                continue;
            }

            if (!this.hasPlayerClass(team, clazz)) {
                this.getRandomizedPlayerClass(team, this.settings.getPlayerClass(clazz.getClassName()));
            }
        }
    }

    private boolean hasPlayerClass(EnumTeam team, EnumPlayerClass clazz) {
        if (!team.equals(EnumTeam.RED) && !team.equals(EnumTeam.BLU)) {
            return true;
        }

        for (COKPlayer player : this.getTeamData(team).getPlayerList().values()) {
            if (player.getPlayerClass() == null) {
                continue;
            }
            if (player.getPlayerClass().getClassName().equals(clazz.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Pause the game
     */
    public void pauseGame() {
        this.setGameState(GameState.PAUSED);
        this.sendMessageToAll(ChatColor.RED, "The game is now paused!");
    }

    /**
     * Unpause the game
     */
    public void unpauseGame() {
        this.setGameState(GameState.RUNNING);
        this.sendMessageToAll(ChatColor.GREEN, "The game is no longer paused!");
    }

    /**
     * Close the game
     */
    public void closeGame() {
        this.stopGame();
        for (COKPlayer player : this.playerList.values()) {
            this.showPlayer(player);
        }
        this.sendMessageToAll(ChatColor.RED, "The game has been closed!");
        this.playerList.clear();
    }

    public boolean isBaseBlock(EnumTeam team, BlockVector vector) {
        return this.teamData.get(team).isBaseBlock(vector, this.settings.getBaseHeight());
    }

    public boolean isBaseComplete(EnumTeam team) {
        return this.teamData.get(team).isBaseComplete(this.settings.getBaseHeight(), this.settings.getBaseTypeID(), this.settings.getBaseSubID());
    }

    // ///////////////////////////////////////////////////////////////
    //
    // Getter and setter
    //
    // ///////////////////////////////////////////////////////////////

    public String getGameName() {
        return gameName;
    }

    private void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean isStopped() {
        return this.gameState.equals(GameState.STOPPED);
    }

    public boolean isRunning() {
        return this.gameState.equals(GameState.RUNNING);
    }

    public boolean isPaused() {
        return this.gameState.equals(GameState.PAUSED);
    }

    public void setSettings(GameSettings settings) {
        this.settings = settings;
    }

    public GameSettings getSettings() {
        return settings;
    }

    public TeamData getTeamData(EnumTeam team) {
        return this.teamData.get(team);
    }

    public HashMap<EnumTeam, TeamData> getAllTeamData() {
        return this.teamData;
    }

    public void punish(int amount, EnumTeam team, boolean showMessage) {
        if (showMessage) {
            this.sendMessageToAll(ChatColor.GOLD, team.getFullTeamName(ChatColor.GOLD) + " gets a punishment of " + amount + " blocks!");
        }
        this.getTeamData(team).addBlocks(amount, this);
    }

    public void hidePlayer(COKPlayer player) {
        for (COKPlayer otherPlayer : this.getTeamData(EnumTeam.RED).getPlayerList().values()) {
            otherPlayer.getBukkitPlayer().hidePlayer(player.getBukkitPlayer());
        }

        for (COKPlayer otherPlayer : this.getTeamData(EnumTeam.BLU).getPlayerList().values()) {
            otherPlayer.getBukkitPlayer().hidePlayer(player.getBukkitPlayer());
        }
    }

    public void showPlayer(COKPlayer player) {
        for (COKPlayer otherPlayer : this.getTeamData(EnumTeam.RED).getPlayerList().values()) {
            otherPlayer.getBukkitPlayer().showPlayer(player.getBukkitPlayer());
        }

        for (COKPlayer otherPlayer : this.getTeamData(EnumTeam.BLU).getPlayerList().values()) {
            otherPlayer.getBukkitPlayer().showPlayer(player.getBukkitPlayer());
        }
    }
}
