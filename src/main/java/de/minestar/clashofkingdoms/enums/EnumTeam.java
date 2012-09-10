package de.minestar.clashofkingdoms.enums;

public enum EnumTeam {
    NONE("NONE"),

    RED("RED"),

    BLU("BLU"),

    REF("REFEREE");

    private final String teamName;

    private EnumTeam(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }
}
