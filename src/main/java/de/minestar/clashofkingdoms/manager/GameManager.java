package de.minestar.clashofkingdoms.manager;

import java.util.HashMap;

import org.apache.commons.lang.Validate;

import de.minestar.clashofkingdoms.data.COKGame;
import de.minestar.clashofkingdoms.data.COKPlayer;

public class GameManager {

    private HashMap<String, COKGame> gameList;
    private HashMap<String, COKPlayer> playerList;

    public GameManager() {
        this.gameList = new HashMap<String, COKGame>();
        this.playerList = new HashMap<String, COKPlayer>();
    }

    // ///////////////////////////////////////////////////////////////
    //
    // Gamerelated stuff
    //
    // ///////////////////////////////////////////////////////////////

    public COKGame getGame(String gameName) {
        return this.gameList.get(gameName.toLowerCase());
    }

    public boolean gameExists(String gameName) {
        return (this.getGame(gameName) != null);
    }

    public COKGame createGame(String gameName) {
        if (this.gameExists(gameName)) {
            return null;
        }

        // finally create the new game
        COKGame game = new COKGame(gameName);
        this.gameList.put(gameName.toLowerCase(), game);
        return game;
    }

    public COKGame closeGame(String gameName) {
        // get the game
        COKGame game = this.getGame(gameName);

        // close the game and remove it
        if (game != null) {
            game.closeGame();
            this.gameList.remove(gameName.toLowerCase());
        }
        return game;
    }

    // ///////////////////////////////////////////////////////////////
    //
    // Playerrelated stuff
    //
    // ///////////////////////////////////////////////////////////////

    public COKPlayer getPlayer(String playerName) {
        return this.playerList.get(playerName);
    }

    public boolean isPlayerInAnyGame(String name) {
        return (this.getPlayer(name) != null);
    }

    public boolean addToPlayerList(COKPlayer player) {
        Validate.notNull(player);
        if (this.isPlayerInAnyGame(player.getPlayerName())) {
            return false;
        }
        this.playerList.put(player.getPlayerName(), player);
        return true;
    }

    public boolean removeFromPlayerList(COKPlayer player) {
        Validate.notNull(player);
        if (!this.isPlayerInAnyGame(player.getPlayerName())) {
            return false;
        }
        this.playerList.remove(player.getPlayerName());
        return true;
    }
}
