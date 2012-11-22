package de.minestar.library.commandsystem;

public class Argument {

    private final String argument;
    private final ArgumentType type;

    public Argument(String argument, ArgumentType type) {
        this.type = type;
        if (this.isKeyword()) {
            this.argument = "|" + argument.toLowerCase() + "|";
        } else {
            this.argument = argument.toLowerCase();
        }
    }

    public String getArgument() {
        return argument;
    }

    public boolean isOptional() {
        return type.equals(ArgumentType.OPTIONAL);
    }

    public boolean isEndless() {
        return type.equals(ArgumentType.ENDLESS);
    }

    public boolean isKeyword() {
        return type.equals(ArgumentType.KEYWORD);
    }

    public boolean isNeeded() {
        return type.equals(ArgumentType.NEEDED);
    }

    public boolean isUnknown() {
        return type.equals(ArgumentType.UNKNOWN);
    }
}
