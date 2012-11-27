package de.minestar.clashofkingdoms.data;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.clashofkingdoms.COKCore;
import de.minestar.clashofkingdoms.classes.PlayerClass;
import de.minestar.clashofkingdoms.enums.EnumTeam;
import de.minestar.core.MinestarCore;

public class COKPlayer {

    private final Player bukkitPlayer;
    private final String playerName;
    private final COKGame game;
    private EnumTeam team = EnumTeam.SPEC;

    private PlayerClass playerClass = null;

    public COKPlayer(String playerName, COKGame game) {
        this.bukkitPlayer = Bukkit.getPlayerExact(playerName);
        this.playerName = playerName;
        this.game = game;

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
        // update the playerclass
        if (this.playerClass != null) {
            this.game.getRandomizedPlayerClass(this.team, this.playerClass);
        }

        this.team = team;
        this.playerClass = null;
        if (team.equals(EnumTeam.SPEC)) {
            game.hidePlayer(this);
        } else {
            game.showPlayer(this);
        }

        if (isInTeam(EnumTeam.REF) || isInTeam(EnumTeam.SPEC)) {
            this.getBukkitPlayer().setAllowFlight(true);
            MinestarCore.getPlayer(this.getBukkitPlayer()).setBoolean("flight.forceCheck", false);
            MinestarCore.getPlayer(this.getBukkitPlayer()).setBoolean("flight.allowFlight", true);
        } else {
            this.getBukkitPlayer().setAllowFlight(false);
            MinestarCore.getPlayer(this.getBukkitPlayer()).setBoolean("flight.forceCheck", true);
            MinestarCore.getPlayer(this.getBukkitPlayer()).setBoolean("flight.allowFlight", false);
        }
    }

    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    public void setPlayerClass(PlayerClass playerClass) {
        this.playerClass = playerClass;
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    public boolean isInTeam(EnumTeam otherTeam) {
        return this.getTeam().equals(otherTeam);
    }

    public boolean equals(COKPlayer other) {
        return this.playerName.equalsIgnoreCase(other.playerName);
    }
}
