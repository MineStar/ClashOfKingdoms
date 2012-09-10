package de.minestar.clashofkingdoms.data;

import java.util.HashMap;
import java.util.Map.Entry;

import de.minestar.clashofkingdoms.utils.BlockVector;

public class BlockBase {

    private int height = 5;
    private HashMap<BlockVector, BaseBlock> baseBlocks;

    public BlockBase() {
        this.baseBlocks = new HashMap<BlockVector, BaseBlock>();
    }

    public boolean isBase(BlockVector vector) {
        for (Entry<BlockVector, BaseBlock> entry : this.baseBlocks.entrySet()) {
            if (entry.getValue().isBase(vector, this.height)) {
                return true;
            }
        }
        return false;
    }

    public boolean registerBaseBlock(BlockVector vector) {
        if (this.baseBlocks.containsKey(vector) || this.isBase(vector)) {
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

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public int getBlockCount() {
        return this.baseBlocks.size();
    }
}
