package de.minestar.clashofkingdoms.classes;

public enum EnumPlayerClass {

    LEADER("Leader", LeaderClass.class),

    ARCHER("Archer", ArcherClass.class),

    KNIGHT("Knight", KnightClass.class),

    REFEREE("Referee", RefereeClass.class);

    private final String typeName;
    private final Class<? extends PlayerClass> clazz;

    private EnumPlayerClass(String typeName, Class<? extends PlayerClass> clazz) {
        this.typeName = typeName;
        this.clazz = clazz;
    }

    public String getTypeName() {
        return typeName;
    }

    public Class<? extends PlayerClass> getClazz() {
        return clazz;
    }

    public static EnumPlayerClass byType(String text) {
        for (EnumPlayerClass type : EnumPlayerClass.values()) {
            if (type.getTypeName().equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }
}
