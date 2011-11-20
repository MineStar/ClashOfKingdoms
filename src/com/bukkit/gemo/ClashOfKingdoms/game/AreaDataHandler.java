package com.bukkit.gemo.ClashOfKingdoms.game;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;

public class AreaDataHandler {

    public static String saveArea(String areaName, Chunk chunk1, Chunk chunk2) {
        // CHECK CHUNKS
        if (chunk1 == null || chunk2 == null)
            return "You must select 2 Chunks.";

        if (!chunk1.getWorld().getName().equalsIgnoreCase(chunk2.getWorld().getName()))
            return "Both Chunks must be in the same world.";

        try {
            // GET VARS
            World world = chunk1.getWorld();
            Point minChunk = new Point(Math.min(chunk1.getX(), chunk2.getX()), Math.min(chunk1.getZ(), chunk2.getZ()));
            Point maxChunk = new Point(Math.max(chunk1.getX(), chunk2.getX()), Math.max(chunk1.getZ(), chunk2.getZ()));
            DirectorChunkSnapshot snapshot = null;
            File dir = new File("plugins/DirectorsPlugin/Areas/");
            dir.mkdirs();

            // DELETE OLD FILE
            File file = new File("plugins/DirectorsPlugin/Areas/" + areaName + ".dp");
            if (file.exists())
                file.delete();

            // WRITE DATA TO FILE
            int count = 0;
            FileOutputStream fos = new FileOutputStream("plugins/DirectorsPlugin/Areas/" + areaName + ".dp");
            for (int x = minChunk.x; x <= maxChunk.x; x++) {
                for (int z = minChunk.y; z <= maxChunk.y; z++) {
                    snapshot = DirectorChunkSnapshot.getSnapshot(world.getChunkAt(x, z));
                    fos.write(snapshot.getAllData());
                    count++;
                }
            }
            fos.close();

            // RETURN
            return "Area saved! ( " + count + " Chunks )";
        } catch (Exception e) {
            // CATCH ERROR
            e.printStackTrace();
            return "Error while writing File!";
        }
    }

    public static String resetArea(String areaName, String worldName, Point chunkPos1, Point chunkPos2) {
        // GET WORLD
        World world = Bukkit.getServer().getWorld(worldName);
        if (world == null) {
            return "World '" + worldName + "' not found!";
        }

        // GET FIRST CHUNK
        Chunk chunk1 = world.getChunkAt(chunkPos1.x, chunkPos1.y);
        if (chunk1 == null) {
            return "There is no Chunk at " + chunkPos1.x + "/" + chunkPos1.y + "! (Chunk 1)";
        }

        // GET SECOND CHUNK
        Chunk chunk2 = world.getChunkAt(chunkPos1.x, chunkPos1.y);
        if (chunk2 == null) {
            return "There is no Chunk at " + chunkPos2.x + "/" + chunkPos2.y + "! (Chunk 2)";
        }

        // CALL METHOD
        return resetArea(areaName, chunk1, chunk2);
    }

    public static String resetArea(String areaName, Chunk chunk1, Chunk chunk2) {
        // CHECK CHUNKS
        if (chunk1 == null || chunk2 == null)
            return "You must select 2 Chunks.";

        if (!chunk1.getWorld().getName().equalsIgnoreCase(chunk2.getWorld().getName()))
            return "Both Chunks must be in the same world.";

        try {
            // GET VARS
            World world = chunk1.getWorld();
            Point minChunk = new Point(Math.min(chunk1.getX(), chunk2.getX()), Math.min(chunk1.getZ(), chunk2.getZ()));
            Point maxChunk = new Point(Math.max(chunk1.getX(), chunk2.getX()), Math.max(chunk1.getZ(), chunk2.getZ()));
            ArrayList<DirectorChunkSnapshot> snapshot = new ArrayList<DirectorChunkSnapshot>();
            String worldname = world.getName();
            File dir = new File("plugins/DirectorsPlugin/Areas/");
            dir.mkdirs();

            // CHECK FILE EXISTS
            if (!areaExists(areaName))
                return "No Area named '" + areaName + "' defined!";

            // READ DATA FROM FILE
            FileInputStream fos = new FileInputStream("plugins/DirectorsPlugin/Areas/" + areaName + ".dp");
            for (int x = minChunk.x; x <= maxChunk.x; x++) {
                for (int z = minChunk.y; z <= maxChunk.y; z++) {
                    byte[] data = new byte[81920];
                    fos.read(data);
                    snapshot.add(new DirectorChunkSnapshot(x, z, worldname, data));
                }
            }
            fos.close();

            // RESET BLOCKS
            CraftWorld cworld = (CraftWorld) world;
            net.minecraft.server.World nativeWorld = cworld.getHandle();
            int count = 0;
            int chunkX, chunkZ;
            DirectorChunkSnapshot thisSnapshot = null;
            DirectorChunkSnapshot currentSnapshot;
            for (int x = minChunk.x; x <= maxChunk.x; x++) {
                chunkX = 16 * x;
                for (int z = minChunk.y; z <= maxChunk.y; z++) {
                    currentSnapshot = DirectorChunkSnapshot.getSnapshot(cworld.getChunkAt(x, z));
                    chunkZ = 16 * z;
                    thisSnapshot = snapshot.get(count);
                    count++;
                    for (int blockX = 0; blockX < 16; blockX++) {
                        for (int blockZ = 0; blockZ < 16; blockZ++) {
                            for (int blockY = 0; blockY < 128; blockY++) {
                                if (thisSnapshot.getBlockTypeId(blockX, blockY, blockZ) != currentSnapshot.getBlockTypeId(blockX, blockY, blockZ) || thisSnapshot.getBlockData(blockX, blockY, blockZ) != currentSnapshot.getBlockData(blockX, blockY, blockZ)) {
                                    nativeWorld.setTypeIdAndData(blockX + chunkX, blockY, blockZ + chunkZ, thisSnapshot.getBlockTypeId(blockX, blockY, blockZ), thisSnapshot.getBlockData(blockX, blockY, blockZ));
                                }
                            }
                        }
                    }
                }
            }

            // RETURN
            return "Area loaded! ( " + snapshot.size() + " Chunks )";
        } catch (Exception e) {
            // CATCH ERROR
            e.printStackTrace();
            return "Error while loading File!";
        }
    }

    public static boolean areaExists(String name) {
        return new File("plugins/DirectorsPlugin/Areas/" + name + ".dp").exists();
    }
}
