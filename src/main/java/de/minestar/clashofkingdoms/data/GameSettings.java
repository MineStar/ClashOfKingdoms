package de.minestar.clashofkingdoms.data;

import java.util.HashMap;

import org.bukkit.Material;

import de.minestar.clashofkingdoms.enums.EnumTeam;
import de.minestar.clashofkingdoms.utils.BlockVector;

public class GameSettings {

    private int baseHeight;
    private int baseTypeID;
    private byte baseSubID;

    private HashMap<EnumTeam, BlockVector> spawnList;

    public GameSettings() {
        this.baseHeight = 5;
        this.baseTypeID = Material.STONE.getId();
        this.baseSubID = (byte) 0;
        this.spawnList = new HashMap<EnumTeam, BlockVector>();
    }

    public BlockVector getSpawn(EnumTeam team) {
        return this.spawnList.get(team);
    }

    public boolean isSpawnSet(EnumTeam team) {
        return (this.spawnList.get(team) != null);
    }

    public int getBaseHeight() {
        return baseHeight;
    }

    public int getBaseTypeID() {
        return baseTypeID;
    }

    public byte getBaseSubID() {
        return baseSubID;
    }
}
