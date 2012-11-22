package de.minestar.clashofkingdoms.enums;

public enum EnumTeam {
    NONE("NONE", (byte) 0),

    RED("RED", (byte) 14),

    BLU("BLU", (byte) 11),

    REF("REF", (byte) 15);

    private final String teamName;
    private final byte subID;

    private EnumTeam(String teamName, byte subID) {
        this.teamName = teamName;
        this.subID = subID;
    }

    public String getTeamName() {
        return teamName;
    }

    public byte getSubID() {
        return subID;
    }

    public static EnumTeam byString(String text) {
        for (EnumTeam team : EnumTeam.values()) {
            if (team.getTeamName().equalsIgnoreCase(text)) {
                return team;
            }
        }
        return EnumTeam.NONE;
    }
}
