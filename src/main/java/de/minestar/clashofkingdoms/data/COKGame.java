package de.minestar.clashofkingdoms.data;

import java.util.HashMap;

import org.bukkit.ChatColor;

import de.minestar.clashofkingdoms.COKCore;
import de.minestar.clashofkingdoms.enums.EnumTeam;
import de.minestar.clashofkingdoms.enums.GameState;
import de.minestar.clashofkingdoms.manager.GameManager;

public class COKGame {

    private GameManager gameManager;

    private final String gameName;
    private HashMap<String, COKPlayer> playerList;
    private GameState gameState = GameState.STOPPED;

    // TEAMDATA
    private HashMap<EnumTeam, TeamData> teamData;

    public COKGame(String gameName) {
        this.gameManager = COKCore.gameManager;
        this.gameName = gameName;
        this.playerList = new HashMap<String, COKPlayer>();

        // TeamData
        this.teamData = new HashMap<EnumTeam, TeamData>();
        this.teamData.put(EnumTeam.BLU, new TeamData(EnumTeam.BLU));
        this.teamData.put(EnumTeam.RED, new TeamData(EnumTeam.RED));
        this.teamData.put(EnumTeam.NONE, new TeamData(EnumTeam.NONE));
        this.teamData.put(EnumTeam.REF, new TeamData(EnumTeam.REF));
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
        if (player != null && this.gameManager.removeFromPlayerList(player)) {
            this.teamData.get(player.getTeam()).removePlayer(player);
            this.teamData.get(newTeam).addPlayer(player);
            return true;
        }
        return false;
    }

    public boolean playerJoinGame(String playerName, EnumTeam team) {
        COKPlayer player = new COKPlayer(playerName, this);
        if (this.gameManager.addToPlayerList(player)) {
            this.playerList.put(player.getPlayerName(), player);
            this.teamData.get(team).addPlayer(player);
            return true;
        }
        return false;
    }

    public boolean playerQuitGame(String playerName) {
        COKPlayer player = this.getPlayer(playerName);
        if (player != null && this.gameManager.removeFromPlayerList(player)) {
            this.teamData.get(player.getTeam()).removePlayer(player);
            this.playerList.remove(player.getPlayerName());
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
        this.setGameState(GameState.RUNNING);
    }

    /**
     * Stop the game
     */
    public void stopGame() {
        this.setGameState(GameState.STOPPED);
    }

    /**
     * Pause the game
     */
    public void pauseGame() {
        this.setGameState(GameState.PAUSED);
    }

    /**
     * Unpause the game
     */
    public void unpauseGame() {
        this.setGameState(GameState.RUNNING);
    }

    /**
     * Close the game
     */
    public void closeGame() {
        this.stopGame();
        this.playerList.clear();
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
}
