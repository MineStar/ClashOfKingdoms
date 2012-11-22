package de.minestar.clashofkingdoms.data;

import java.util.HashMap;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;

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

    public boolean isBaseComplete(int baseHeight, int baseTypeID, byte baseSubID) {
        Block block = null;
        for (BaseBlock baseBlock : this.blockBase.getBaseBlocks()) {
            for (int difY = 1; difY <= baseHeight; difY++) {
                block = baseBlock.getVector().getRelative(0, difY, 0).getLocation().getBlock();
                if (block.getType().equals(Material.AIR)) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getBaseBlockCount() {
        return this.blockBase.getBlockCount();
    }

    public boolean isBaseBlock(BlockVector vector, int baseHeight) {
        return this.blockBase.isBase(vector, baseHeight);
    }

    public boolean isRealBaseBlock(BlockVector vector) {
        return this.blockBase.isRealBaseBlock(vector);
    }

    public boolean registerBaseBlock(BlockVector vector, int baseHeight) {
        return this.blockBase.registerBaseBlock(vector, baseHeight);
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

    public int getPlayerCount() {
        return this.playerList.size();
    }

    public void resetBase(int baseHeight) {
        Block block = null;
        for (BaseBlock baseBlock : this.blockBase.getBaseBlocks()) {
            for (int difY = 0; difY <= baseHeight; difY++) {
                block = baseBlock.getVector().getRelative(0, difY, 0).getLocation().getBlock();
                if (difY > 0) {
                    block.setType(Material.AIR);
                } else {
                    block.setTypeIdAndData(Material.WOOL.getId(), this.team.getSubID(), true);
                }
            }
        }
    }
}
