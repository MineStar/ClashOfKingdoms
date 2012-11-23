package de.minestar.clashofkingdoms.data;

import org.bukkit.block.Block;

import de.minestar.clashofkingdoms.utils.BlockVector;

public class BaseBlock {

    private final BlockVector vector;

    public BaseBlock(BlockVector vector) {
        this.vector = vector.clone();
    }

    public boolean isBase(BlockVector vector, final int HEIGHT) {
        if (vector.getX() != this.vector.getX()) {
            return false;
        }
        if (vector.getZ() != this.vector.getZ()) {
            return false;
        }

        return (vector.getY() >= this.vector.getY() && vector.getY() <= this.vector.getY() + HEIGHT);
    }

    public BlockVector getVector() {
        return vector;
    }

    public boolean addBlock(COKGame game) {
        for (int y = 1; y <= game.getSettings().getBaseHeight(); y++) {
            Block block = this.vector.getRelative(0, y, 0).getLocation().getBlock();
            if (block.getTypeId() != game.getSettings().getBaseTypeID() || block.getData() != game.getSettings().getBaseSubID()) {
                block.setTypeIdAndData(game.getSettings().getBaseTypeID(), game.getSettings().getBaseSubID(), true);
                return true;
            }
        }
        return false;
    }
}
