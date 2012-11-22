package de.minestar.clashofkingdoms.data;

import java.util.HashMap;

import org.bukkit.ChatColor;

import de.minestar.clashofkingdoms.COKCore;
import de.minestar.clashofkingdoms.enums.EnumTeam;
import de.minestar.clashofkingdoms.enums.GameState;
import de.minestar.clashofkingdoms.manager.GameManager;
import de.minestar.clashofkingdoms.utils.BlockVector;

public class COKGame {

    private static final String GAME_JOIN = "'%s' has joined the game!";
    private static final String GAME_QUIT = "'%s' has left the game!";
    private static final String TEAM_SWITCH = "'%s' is now in Team %s!";
    private static final String TEAM_TOO_FEW = "Team %s has too few players!";

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
        this.addTeamData(EnumTeam.NONE);
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
            this.sendMessageToAll(ChatColor.GRAY, String.format(TEAM_SWITCH, playerName, newTeam.name()));
            return true;
        }
        return false;
    }

    public void onPlayerDisconnect(String playerName) {
        // TODO: interact with classes

        this.playerQuitGame(playerName);
    }

    public void onPlayerDeath(String playerName) {
        // TODO: interact with classes
    }

    public boolean playerJoinGame(String playerName, EnumTeam team) {
        COKPlayer player = new COKPlayer(playerName, this);
        if (this.gameManager.addToPlayerList(player)) {
            player.setTeam(team);
            this.playerList.put(player.getPlayerName(), player);
            this.teamData.get(team).addPlayer(player);
            this.sendMessageToAll(ChatColor.GRAY, String.format(GAME_JOIN, playerName));
            return true;
        }
        return false;
    }

    public boolean playerQuitGame(String playerName) {
        COKPlayer player = this.getPlayer(playerName);
        if (player != null && this.gameManager.removeFromPlayerList(player)) {
            player.clearInventory();
            this.teamData.get(player.getTeam()).removePlayer(player);
            this.playerList.remove(player.getPlayerName());
            this.sendMessageToAll(ChatColor.GRAY, String.format(GAME_QUIT, playerName));
            if (this.teamData.get(player.getTeam()).getPlayerCount() < MIN_PLAYERS_PER_TEAM) {
                this.sendMessageToAll(ChatColor.RED, String.format(TEAM_TOO_FEW, player.getTeam().name()));
                this.pauseGame();
            }
            return true;
        }
        return false;
    }

    // ///////////////////////////////////////////////////////////////
    //
    // Messagerelated stuff
    //
    // ///////////////////////////////////////////////////////////////

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

        // CHECK PLAYERCOUNT
        if (this.teamData.get(EnumTeam.RED).getPlayerCount() < MIN_PLAYERS_PER_TEAM) {
            this.sendMessageToAll(ChatColor.RED, String.format(TEAM_TOO_FEW, EnumTeam.RED.name()));
            return;
        }

        // CHECK PLAYERCOUNT
        if (this.teamData.get(EnumTeam.BLU).getPlayerCount() < MIN_PLAYERS_PER_TEAM) {
            this.sendMessageToAll(ChatColor.RED, String.format(TEAM_TOO_FEW, EnumTeam.BLU.name()));
            return;
        }

        this.resetGame();
        this.setGameState(GameState.RUNNING);
        this.sendMessageToAll(ChatColor.GREEN, "The game has started!");
    }

    /**
     * Stop the game
     */
    public void stopGame() {
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
            }
        }
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

    public GameSettings getSettings() {
        return settings;
    }

    public TeamData getTeamData(EnumTeam team) {
        return this.teamData.get(team);
    }
}
