package de.minestar.clashofkingdoms.enums;

import org.bukkit.ChatColor;

public enum EnumTeam {
    SPEC("SPEC", (byte) 0, ChatColor.GRAY),

    RED("RED", (byte) 14, ChatColor.RED),

    BLU("BLU", (byte) 11, ChatColor.BLUE),

    REF("REF", (byte) 15, ChatColor.GOLD);

    private final String teamName;
    private final byte subID;
    private final ChatColor color;

    private EnumTeam(String teamName, byte subID, ChatColor color) {
        this.teamName = teamName;
        this.subID = subID;
        this.color = color;
    }

    public String getTeamName() {
        return teamName;
    }

    public byte getSubID() {
        return subID;
    }

    public String getFullTeamName(ChatColor afterColor) {
        return this.color + "TEAM " + teamName + afterColor;
    }

    public static EnumTeam byString(String text) {
        for (EnumTeam team : EnumTeam.values()) {
            if (team.getTeamName().equalsIgnoreCase(text)) {
                return team;
            }
        }
        return EnumTeam.SPEC;
    }
}
