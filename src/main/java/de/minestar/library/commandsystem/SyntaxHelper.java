package de.minestar.library.commandsystem;

import java.util.ArrayList;

public class SyntaxHelper {

    private static String KEYS_MUST_ARGS = "<>";
    private static String KEYS_OPT_ARGS = "[]";
    private static String KEYS_INF_ARG = "...";

    // ///////////////////////////////////////////////////////////////
    //
    // STATIC METHODS
    //
    // ///////////////////////////////////////////////////////////////

    public static boolean isSyntaxValid(String syntax) {
        int mustCount = 0, optCount = 0;
        char key;

        for (int index = 0; index < syntax.length(); index++) {
            if (optCount < 0 || mustCount < 0) {
                return false;
            }

            key = syntax.charAt(index);
            if (key == KEYS_MUST_ARGS.charAt(0)) {
                ++mustCount;
                continue;
            }
            if (key == KEYS_MUST_ARGS.charAt(1)) {
                --mustCount;
                continue;
            }
            if (key == KEYS_OPT_ARGS.charAt(0)) {
                ++optCount;
                continue;
            }
            if (key == KEYS_OPT_ARGS.charAt(1)) {
                --optCount;
                continue;
            }
        }
        return (optCount == 0 && mustCount == 0);
    }

    /**
     * Remove the syntaxkeys from a given String
     * 
     * @param syntax
     * @return Return the syntax without the syntaxkeys.<br />
     *         <b>Example:</b><br />
     *         [<Player> [Player...]] => <Player> [Player]
     */
    public static String removeSyntaxKeys(String syntax) {
        if (syntax.startsWith(" ")) {
            syntax = syntax.substring(1, syntax.length() - 1);
        }
        return syntax.substring(1, syntax.length() - 1);
    }

    /**
     * Get all arguments for a given Syntax
     * 
     * @param syntax
     * @return All arguments for a given Syntax
     */
    public static ArrayList<String> getArguments(String syntax) {
        ArrayList<String> arguments = new ArrayList<String>();
        char key;
        String argument = "";
        boolean mustLocked = false, optLocked = false;
        int lockIndex = 0;
        for (int index = 0; index < syntax.length(); index++) {
            key = syntax.charAt(index);
            argument += key;

            if (key == KEYS_MUST_ARGS.charAt(0) && !mustLocked && !optLocked) {
                mustLocked = true;
                lockIndex = 1;
                continue;
            }
            if (key == KEYS_MUST_ARGS.charAt(1) && mustLocked) {
                --lockIndex;
                if (lockIndex == 0) {
                    mustLocked = false;
                    arguments.add(argument);
                    argument = "";
                }
                continue;
            }
            if (key == KEYS_OPT_ARGS.charAt(0) && !mustLocked) {
                if (optLocked) {
                    ++lockIndex;
                } else {
                    optLocked = true;
                    lockIndex = 1;
                }
                continue;
            }

            if (key == KEYS_OPT_ARGS.charAt(1) && optLocked) {
                --lockIndex;
                if (lockIndex == 0) {
                    mustLocked = false;
                    arguments.add(argument);
                    argument = "";
                }
                continue;
            }

            if (key == ' ' && !optLocked && !mustLocked) {
                argument = argument.replace(" ", "");
                if (!argument.equalsIgnoreCase(" ") && !argument.equalsIgnoreCase("")) {
                    arguments.add(argument);
                }
                argument = "";
                continue;
            }
        }

        if (argument.length() > 0) {
            arguments.add(argument);
        }
        return arguments;
    }

    /**
     * Get the ArgumentType for a given argument
     * 
     * @param argument
     * @param currentIndex
     * @return the ArgumentType for the given argument
     */
    public static ArgumentType getArgumentType(String argument, int currentIndex) {
        if (argument.startsWith(String.valueOf(KEYS_MUST_ARGS.charAt(0))) && argument.endsWith(String.valueOf(KEYS_MUST_ARGS.charAt(1)))) {
            return ArgumentType.NEEDED;
        } else if (argument.startsWith(String.valueOf(KEYS_OPT_ARGS.charAt(0))) && argument.endsWith(String.valueOf(KEYS_OPT_ARGS.charAt(1)))) {
            if (argument.contains(KEYS_INF_ARG)) {
                return ArgumentType.ENDLESS;
            } else {
                return ArgumentType.OPTIONAL;
            }
        } else if (currentIndex >= 0) {
            if (argument.length() > 0) {
                return ArgumentType.KEYWORD;
            }
        }
        return ArgumentType.UNKNOWN;
    }
}
