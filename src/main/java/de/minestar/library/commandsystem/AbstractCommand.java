package de.minestar.library.commandsystem;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.bukkit.gemo.utils.UtilPermissions;

import de.minestar.library.commandsystem.annotations.Arguments;
import de.minestar.library.commandsystem.annotations.Description;
import de.minestar.library.commandsystem.annotations.Execution;
import de.minestar.library.commandsystem.annotations.Label;
import de.minestar.library.commandsystem.annotations.PermissionNode;
import de.minestar.minestarlibrary.utils.ChatUtils;
import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.minestarlibrary.utils.PlayerUtils;

public abstract class AbstractCommand {

    // String for no permissions
    public final static String NO_PERMISSION = "Du darfst dieses Kommando nicht benutzen!";

    // the pluginName (for easier use with ConsoleUtils/PlayerUtils)
    private String pluginName;

    // vars for the handling of supercommands
    private boolean superCommand = false; // true = this command has subcommands ; false = this command has no subcommands
    private boolean executeSuperCommand = false; // true = execute this supercommand ; false = only print the syntax

    // the parent command (null, if this command doesn't have a parent)
    private AbstractCommand parentCommand;

    // argumentcounts
    private int minimumArgumentCount = -1;
    private int maximumArgumentCount = -1;

    // needed vars for each command
    private final String commandLabel;
    private String arguments, permissionNode, description;

    // list of subcommands
    public HashMap<String, AbstractCommand> subCommands;

    /**
     * Constructor
     */
    public AbstractCommand() {
        // set the pluginName & the parent command
        this.pluginName = "";
        this.parentCommand = null;

        // fetch the annotations for this command
        Label labelAnnotation = this.getClass().getAnnotation(Label.class);
        Arguments argumentAnnotation = this.getClass().getAnnotation(Arguments.class);
        PermissionNode nodeAnnotation = this.getClass().getAnnotation(PermissionNode.class);
        Description descriptionAnnotation = this.getClass().getAnnotation(Description.class);
        Execution executeSuperCommandAnnotation = this.getClass().getAnnotation(Execution.class);

        // Save the label. NOTE: If the label is not set, this will throw an RuntimeException and the command won't be registered.
        if (labelAnnotation == null) {
            this.commandLabel = "";
            throw new RuntimeException("Could not create command '" + this.getClass().getSimpleName() + "'! Commandlabel is missing!");
        } else {
            // replace all SPACES
            this.commandLabel = labelAnnotation.label().replace(" ", "");

            // do we have at least one char?
            if (this.commandLabel.length() < 1) {
                throw new RuntimeException("Could not create command '" + this.getClass().getSimpleName() + "'! Commandlabel is missing!");
            }
        }

        // get the argument
        if (argumentAnnotation == null) {
            this.arguments = "";
        } else {
            this.arguments = argumentAnnotation.arguments();
        }

        // get the permissionnode
        if (nodeAnnotation == null) {
            this.permissionNode = "";
        } else {
            this.permissionNode = nodeAnnotation.node();
        }

        // get the description
        if (descriptionAnnotation == null) {
            this.description = "";
        } else {
            this.description = descriptionAnnotation.description();
        }

        // get the execution-params
        if (executeSuperCommandAnnotation == null) {
            this.executeSuperCommand = false;
        } else {
            this.executeSuperCommand = executeSuperCommandAnnotation.executeSuperCommand();
        }

        // count the arguments for this command
        this.countArguments();
    }

    /**
     * @param player
     * @return True, if the sender has enough permission to use the command Or the permissionnode is empty, so everybody can use it
     */
    private final boolean hasPermission(Player player) {
        return permissionNode.length() == 0 || UtilPermissions.playerCanUseCommand(player, this.getPermissionNode());
    }

    /**
     * Check if the argumentcount was correct for this command
     * 
     * @param arguments
     * @return <b>true</b> if it is correct, otherwise <b>false</b>
     */
    public final boolean isSyntaxCorrect(String[] arguments) {
        return (!this.hasOptionalArguments() && arguments.length == this.getMinimumArgumentCount()) || (this.hasOptionalArguments() && arguments.length >= this.getMinimumArgumentCount() && arguments.length <= this.getMaximumArgumentCount());
    }

    /**
     * Execute the command. This will also check the permissions for players.
     * 
     * @param sender
     * @param arguments
     */
    public final void run(CommandSender sender, String[] arguments) {
        if (sender instanceof Player) {
            // get the player
            Player player = (Player) sender;

            // check the permission
            if (!hasPermission(player)) {
                PlayerUtils.sendError((Player) sender, pluginName, NO_PERMISSION);
                return;
            }

            // execute the command for a player
            this.execute(player, arguments);
        } else {
            // execute the command for the console
            this.execute((ConsoleCommandSender) sender, arguments);
        }
    }

    /**
     * Execute the command for a player.
     * 
     * @param console
     * @param arguments
     */
    public void execute(Player player, String[] arguments) {
        ConsoleUtils.printError(pluginName, "Das Kommando '" + this.getSyntax() + "' kann nicht von einem Spieler ausgeführt werden!");
    }

    /**
     * Execute the command for the console.
     * 
     * @param console
     * @param arguments
     */
    public void execute(ConsoleCommandSender console, String[] arguments) {
        ConsoleUtils.printError(pluginName, "Das Kommando '" + this.getSyntax() + "' kann nicht von der Konsole ausgeführt werden!");
    }

    /**
     * This method is automatically called on registration of the command. If we have subcommands, this is the place to register them in our commands.
     */
    protected void createSubCommands() {
    }

    /**
     * This method will initialize the subcommands for this command. This method is automatically called.
     */
    public final void initializeSubCommands() {
        this.createSubCommands();
        if (this.subCommands != null) {
            this.superCommand = (this.subCommands.size() > 0);
        } else {
            this.superCommand = false;
        }
    }

    /**
     * Method to count the arguments for the commands syntax.
     */
    private final void countArguments() {
        if (this.minimumArgumentCount != -1 && this.maximumArgumentCount != -1) {
            return;
        }
        this.minimumArgumentCount = 0;
        this.maximumArgumentCount = 0;
        char key;
        // iterate over the string
        for (int i = 0; i < this.arguments.length(); ++i) {
            key = this.arguments.charAt(i);
            if (key == '<') {
                // we have an '<' : this is a needed parameter
                ++this.minimumArgumentCount;
                ++this.maximumArgumentCount;
            } else if (key == '[') {
                // we have an '[' : this is an optional parameter
                ++this.maximumArgumentCount;
            }
        }
    }

    /**
     * Does this command have optional arguments?
     * 
     * @return <b>true</b> if this command has optional arguments, otherwise <b>false</b>
     */
    public final boolean hasOptionalArguments() {
        return (this.minimumArgumentCount != this.maximumArgumentCount);
    }

    /**
     * Get the minimum argumentcount
     * 
     * @return the minimum argumentcount
     */
    public final int getMinimumArgumentCount() {
        return minimumArgumentCount;
    }

    /**
     * Get the maximum argumentcount
     * 
     * @return the maximum argumentcount
     */
    public final int getMaximumArgumentCount() {
        return maximumArgumentCount;
    }

    /**
     * Should this Command be executed (only if it is a supercommand)
     * 
     * @return <b>true</b> if the command should be executed, otherwise <b>false</b>
     */
    public final boolean isExecuteSuperCommand() {
        return executeSuperCommand;
    }

    /**
     * Is this command a supercommand?
     * 
     * @return <b>true</b> if the command is a supercommand, <b>false</b> if it is a normal command
     */
    public final boolean isSuperCommand() {
        return superCommand;
    }

    /**
     * Set the parent of this command
     * 
     * @param command
     */
    private final void setParentCommand(AbstractCommand command) {
        this.parentCommand = command;
        if (command != null) {
            this.pluginName = command.pluginName;
        }
    }

    /**
     * Update the subcommands of this command. This method is called automatically.
     */
    public final void updateSubCommands() {
        for (AbstractCommand command : this.subCommands.values()) {
            command.setParentCommand(this);
        }
    }

    /**
     * Get the label of this command.
     * 
     * @return the label
     */
    public final String getLabel() {
        return commandLabel;
    }

    /**
     * Set the pluginName
     * 
     * @param pluginName
     */
    public final void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    /**
     * Get all subcommands
     * 
     * @return all subcommands
     */
    public final ArrayList<AbstractCommand> getSubCommands() {
        if (this.subCommands != null) {
            return new ArrayList<AbstractCommand>(subCommands.values());
        } else {
            return new ArrayList<AbstractCommand>();
        }
    }

    /**
     * Get the argumentstring.
     * 
     * @return the argumentstring
     */
    public final String getArguments() {
        return arguments;
    }

    /**
     * Get the syntax without the arguments (but with all supercommands) for this command.
     * 
     * @return the syntax without the arguments, but with all supercommands
     */
    public final String getCommand() {
        // iterate over every parent and add the label in front of the current syntax
        String syntax = this.getLabel();
        AbstractCommand parent = this.parentCommand;
        while (parent != null) {
            syntax = parent.getLabel() + " " + syntax;
            parent = parent.parentCommand;
        }
        return syntax;
    }

    /**
     * Get the complete syntax for this command.
     * 
     * @return the complete syntax (including supercommands and the argumentstring)
     */
    public final String getSyntax() {
        // iterate over every parent and add the label in front of the current syntax
        String syntax = this.getLabel();
        AbstractCommand parent = this.parentCommand;
        while (parent != null) {
            syntax = parent.getLabel() + " " + syntax;
            parent = parent.parentCommand;
        }
        syntax += " " + this.getArguments();
        return syntax;
    }

    /**
     * Get the permissionnode.
     * 
     * @return the permissionnode
     */
    public final String getPermissionNode() {
        return permissionNode;
    }

    /**
     * Get the description.
     * 
     * @return the description
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Register a new subcommand for this command.
     * 
     * @param command
     * @return <b>true</b> if the command is registered successfully, otherwise <b>false</b>
     */
    protected final boolean registerCommand(AbstractCommand command) {
        // create the map, if it is null
        if (this.subCommands == null) {
            this.subCommands = new HashMap<String, AbstractCommand>();
        }

        // is a subcommand with this label already registered?
        if (this.subCommands.containsKey(command.getLabel())) {
            throw new RuntimeException("Command '" + command.getLabel() + "' is already registered in '" + command.getCommand() + "'!");
        }

        // store the command
        this.subCommands.put(command.getLabel(), command);

        // finally update the command and initialize it
        command.setPluginName(pluginName);
        command.setParentCommand(this);
        command.initializeSubCommands();

        return true;
    }

    /**
     * Print the syntax for this command.
     */
    public final void printWrongSyntax(CommandSender sender) {
        ChatUtils.writeError(sender, this.pluginName, "Falsche Syntax!");
        this.listCommand(sender);
    }

    /**
     * List the command
     */
    public final void listCommand(CommandSender sender) {
        ChatUtils.writeInfo(sender, this.pluginName, this.getSyntax());
        ArrayList<AbstractCommand> subCommands = getSubCommands();
        for (AbstractCommand subCommand : subCommands) {
            ChatUtils.writeInfo(sender, this.pluginName, subCommand.getSyntax());
        }
    }

    public final boolean handleCommand(CommandSender sender, String label, String[] arguments) {
        // cast the label to lowercase
        label = label.toLowerCase();

        // lookup the command
        AbstractCommand command = this.subCommands.get(label);
        if (command == null) {
            if (this.isExecuteSuperCommand()) {
                // make the label the new first argument
                String[] newArguments = new String[arguments.length + 1];
                newArguments[0] = label;

                // add the rest of the arguments to the new array
                System.arraycopy(arguments, 0, newArguments, 1, newArguments.length - 1);

                // check argumentcount & try to execute it
                if (this.isSyntaxCorrect(newArguments)) {
                    // (2.1)

                    // execute the command
                    this.run(sender, newArguments);
                } else {
                    // (2.2)

                    // print the syntax
                    this.printWrongSyntax(sender);
                }
                // this.execute(newArguments);
                return true;
            } else {
                // print the syntax
                this.listCommand(sender);
                return false;
            }
        }

        // (1) : we have a supercommand
        // (2) : We have a normal command

        if (command.isSuperCommand()) {
            // (1)

            // (1.1) -> if we have arguments : copy the array and make the first argument the new label. Then we let the command handle the execution.
            // (1.2) -> otherwise : we will see if the command should be executed (see annotations) and react to it. We will execute it (1.2.1) or just print the syntax (1.2.2).
            if (arguments.length > 0) {
                // (1.1)

                // make the first argument the label
                label = arguments[0];

                // copy the rest of the arguments to a new array
                String[] newArguments = new String[arguments.length - 1];
                System.arraycopy(arguments, 1, newArguments, 0, newArguments.length);

                // handle the command
                command.handleCommand(sender, label, newArguments);
            } else {
                // (1.2)
                if (command.isExecuteSuperCommand()) {
                    // (1.2.1)

                    // execute the command
                    command.run(sender, arguments);
                } else {
                    // (1.2.2)

                    // print the syntax
                    command.listCommand(sender);
                }
            }
            return true;
        } else {
            // (2)

            // check the argumentcount to see if the passed argumentcount is correct for this command. Execute the command if it is true (2.1) , otherwise print the syntax (2.2).

            // check argumentcount & try to execute it
            if (command.isSyntaxCorrect(arguments)) {
                // (2.1)

                // execute the command
                command.run(sender, arguments);
            } else {
                // (2.2)

                // print the syntax
                command.printWrongSyntax(sender);
            }
            return true;
        }
    }
}
