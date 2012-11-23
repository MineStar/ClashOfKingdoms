package de.minestar.clashofkingdoms.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class StringUtils {
    public static String LocationToString(Location location) {
        if (location == null) {
            return "NULL";
        }
        return location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + location.getYaw() + ";" + location.getPitch();
    }

    public static Location LocationFromString(String string) {
        try {
            String[] split = string.split(";");
            World world = Bukkit.getWorld(split[0]);
            if (world == null) {
                return null;
            }

            return new Location(world, Double.valueOf(split[1]), Double.valueOf(split[2]), Double.valueOf(split[3]), Float.valueOf(split[4]), Float.valueOf(split[5]));
        } catch (Exception e) {
            return null;
        }
    }

    public static String BlockVectorToString(BlockVector vector) {
        return StringUtils.LocationToString(vector.getLocation());
    }

    public static BlockVector BlockVectorFromString(String string) {
        Location location = StringUtils.LocationFromString(string);
        if (location == null) {
            return null;
        }
        return new BlockVector(location);
    }
}
