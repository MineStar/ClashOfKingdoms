package de.minestar.clashofkingdoms.data;

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

}
