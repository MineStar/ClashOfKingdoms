package de.minestar.clashofkingdoms.data;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.clashofkingdoms.COKCore;

public class COKPlayer {

    private final Player bukkitPlayer;
    private final String playerName;
    private final COKGame game;

    public COKPlayer(String playerName, COKGame game) {
        this.bukkitPlayer = Bukkit.getPlayerExact(playerName);
        this.playerName = playerName;
        this.game = game;

        Validate.notNull(this.bukkitPlayer);
        if (!this.bukkitPlayer.isOnline()) {
            throw new RuntimeException(COKCore.FULLNAME + "Trying to create COKPlayer '" + playerName + "', but the player is not online!");
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public COKGame getGame() {
        return game;
    }

    public void sendMessage(String message) {
        this.bukkitPlayer.sendMessage(message);
    }

    public void sendMessage(ChatColor color, String message) {
        this.bukkitPlayer.sendMessage(color + message);
    }
}
