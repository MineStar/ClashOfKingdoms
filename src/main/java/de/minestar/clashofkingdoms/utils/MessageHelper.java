package de.minestar.clashofkingdoms.utils;

import org.bukkit.ChatColor;

import de.minestar.clashofkingdoms.COKCore;

public class MessageHelper {

    public static String getFullMessage(ChatColor messageColor, String message) {
        return getFullMessage(ChatColor.AQUA, COKCore.FULLNAME, messageColor, message);
    }

    public static String getFullMessage(ChatColor prefixColor, String prefix, ChatColor messageColor, String message) {
        return prefixColor + "[" + prefix + "] " + messageColor + message;
    }
}
