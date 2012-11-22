package de.minestar.clashofkingdoms.data;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.clashofkingdoms.COKCore;
import de.minestar.clashofkingdoms.enums.EnumTeam;

public class COKPlayer {

    private final Player bukkitPlayer;
    private final String playerName;
    private final COKGame game;
    private EnumTeam team = EnumTeam.NONE;
    private boolean isPunished;

    public COKPlayer(String playerName, COKGame game) {
        this.bukkitPlayer = Bukkit.getPlayerExact(playerName);
        this.playerName = playerName;
        this.game = game;
        this.isPunished = false;

        Validate.notNull(this.bukkitPlayer);
        if (!this.bukkitPlayer.isOnline()) {
            throw new RuntimeException(COKCore.FULLNAME + "Trying to create COKPlayer '" + playerName + "', but the player is not online!");
        }
    }

    public void sendMessage(String message) {
        this.bukkitPlayer.sendMessage(message);
    }

    public void sendMessage(ChatColor color, String message) {
        this.bukkitPlayer.sendMessage(color + message);
    }

    public void clearInventory() {
        this.bukkitPlayer.getInventory().clear();
        this.bukkitPlayer.getInventory().setHelmet(null);
        this.bukkitPlayer.getInventory().setChestplate(null);
        this.bukkitPlayer.getInventory().setLeggings(null);
        this.bukkitPlayer.getInventory().setBoots(null);
    }

    // ///////////////////////////////////////////////////////////////
    //
    // Getter and setter
    //
    // ///////////////////////////////////////////////////////////////

    public String getPlayerName() {
        return playerName;
    }

    public COKGame getGame() {
        return game;
    }

    public EnumTeam getTeam() {
        return team;
    }

    public void setTeam(EnumTeam team) {
        this.team = team;
    }

    public boolean isInTeam(EnumTeam otherTeam) {
        return this.getTeam().equals(otherTeam);
    }

    public boolean isPunished() {
        return isPunished;
    }

    public void setPunished(boolean isPunished) {
        this.isPunished = isPunished;
    }
}
