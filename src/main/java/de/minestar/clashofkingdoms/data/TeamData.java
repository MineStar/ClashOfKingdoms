package de.minestar.clashofkingdoms.data;

import java.util.HashMap;

import org.apache.commons.lang.Validate;

import de.minestar.clashofkingdoms.enums.EnumTeam;
import de.minestar.clashofkingdoms.utils.BlockVector;

public class TeamData {
    private final EnumTeam team;
    private final BlockBase blockBase;
    private BlockVector spawn = null;

    private HashMap<String, COKPlayer> playerList;

    public TeamData(EnumTeam team) {
        this.team = team;
        this.blockBase = new BlockBase();
        this.playerList = new HashMap<String, COKPlayer>();
    }

    public void addPlayer(COKPlayer player) {
        Validate.notNull(player);
        this.playerList.put(player.getPlayerName(), player);
    }

    public void removePlayer(COKPlayer player) {
        Validate.notNull(player);
        this.playerList.remove(player.getPlayerName());
    }

    public int getBaseHeight() {
        return this.blockBase.getHeight();
    }

    public void setBaseHeight(int height) {
        this.blockBase.setHeight(height);
    }

    public int getBaseBlockCount() {
        return this.blockBase.getBlockCount();
    }

    public boolean isBaseBlock(BlockVector vector) {
        return this.blockBase.isBase(vector);
    }

    public boolean registerBaseBlock(BlockVector vector) {
        return this.blockBase.registerBaseBlock(vector);
    }

    public boolean unregisterBaseBlock(BlockVector vector) {
        return this.blockBase.unregisterBaseBlock(vector);
    }

    public void setSpawn(BlockVector spawn) {
        this.spawn = spawn;
    }

    public BlockVector getSpawn() {
        return spawn;
    }

    public EnumTeam getTeam() {
        return team;
    }
}
