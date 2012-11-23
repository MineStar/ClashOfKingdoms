package de.minestar.clashofkingdoms.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import de.minestar.clashofkingdoms.enums.EnumTeam;
import de.minestar.clashofkingdoms.utils.BlockVector;

public class BlockBase {

    private HashMap<BlockVector, BaseBlock> baseBlocks;

    public BlockBase() {
        this.baseBlocks = new HashMap<BlockVector, BaseBlock>();
    }

    public boolean isBase(BlockVector vector, int baseHeight) {
        for (Entry<BlockVector, BaseBlock> entry : this.baseBlocks.entrySet()) {
            if (entry.getValue().isBase(vector, baseHeight)) {
                return true;
            }
        }
        return false;
    }

    public boolean isRealBaseBlock(BlockVector vector) {
        return this.baseBlocks.containsKey(vector);
    }

    public boolean registerBaseBlock(BlockVector vector, int baseHeight) {
        if (this.baseBlocks.containsKey(vector) || this.isBase(vector, baseHeight)) {
            return false;
        }

        BaseBlock baseBlock = new BaseBlock(vector);
        this.baseBlocks.put(vector.clone(), baseBlock);
        return true;
    }

    public boolean unregisterBaseBlock(BlockVector vector) {
        if (!this.baseBlocks.containsKey(vector)) {
            return false;
        }

        this.baseBlocks.remove(vector);
        return true;
    }

    public Collection<BaseBlock> getBaseBlocks() {
        return this.baseBlocks.values();
    }

    public int getBlockCount() {
        return this.baseBlocks.size();
    }

    public void addBlocks(int punishBlocks, COKGame game, EnumTeam team) {
        for (BaseBlock block : this.baseBlocks.values()) {
            if (punishBlocks > 0) {
                if (block.addBlock(game)) {
                    if (game.checkForWinner(team)) {
                        return;
                    }
                    punishBlocks--;
                }
            }
        }
        if (punishBlocks > 0) {
            this.addBlocks(punishBlocks, game, team);
        }
        game.checkForWinner(team);
    }
}
