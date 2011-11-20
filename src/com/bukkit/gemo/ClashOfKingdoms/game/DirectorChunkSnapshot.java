package com.bukkit.gemo.ClashOfKingdoms.game;

import java.awt.Point;

import org.bukkit.Chunk;
import org.bukkit.craftbukkit.CraftChunk;

public class DirectorChunkSnapshot {
    private final int x;
    private final int z;
    private final String worldname;
    private final byte[] blockData;

    public static DirectorChunkSnapshot getSnapshot(Chunk chunk) {
        net.minecraft.server.Chunk nativeChunk = ((CraftChunk) chunk).getHandle();        
        byte[] data = new byte[81920];
        nativeChunk.getData(data, 0, 0, 0, 16, 128, 16, 0);
        return new DirectorChunkSnapshot(chunk.getX(), chunk.getZ(), chunk.getWorld().getName(), data);
    }

    public byte[] getAllData() {
        return this.blockData;
    }
    
    public DirectorChunkSnapshot(int x, int z, String worldname, byte[] blockData) {
        this.x = x;
        this.z = z;
        this.worldname = worldname;
        this.blockData = blockData;
    }

    public String getWorldname() {
        return this.worldname;
    }

    public Point getCoordinates() {
        return new Point(this.x, this.z);
    }

    public int getBlockTypeId(int x, int y, int z) {
        return this.blockData[(x << 11 | z << 7 | y)] & 0xFF;
    }

    public int getBlockData(int x, int y, int z) {
        int off = (x << 10 | z << 6 | y >> 1) + 32768;
        return (y & 0x1) == 0 ? this.blockData[off] & 0xF : this.blockData[off] >> 4 & 0xF;
    }
}
