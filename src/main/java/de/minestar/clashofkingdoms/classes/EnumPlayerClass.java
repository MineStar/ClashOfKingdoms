package de.minestar.clashofkingdoms.classes;

public enum EnumPlayerClass {

    LEADER("Leader", LeaderClass.class),

    ARCHER("Archer", ArcherClass.class),

    KNIGHT("Knight", KnightClass.class),

    REFEREE("Referee", RefereeClass.class);

    private final String className;
    private final Class<? extends PlayerClass> clazz;

    private EnumPlayerClass(String className, Class<? extends PlayerClass> clazz) {
        this.className = className;
        this.clazz = clazz;
    }

    public String getClassName() {
        return className;
    }

    public Class<? extends PlayerClass> getClazz() {
        return clazz;
    }

    public static EnumPlayerClass byType(String text) {
        for (EnumPlayerClass type : EnumPlayerClass.values()) {
            if (type.getClassName().equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }
}
