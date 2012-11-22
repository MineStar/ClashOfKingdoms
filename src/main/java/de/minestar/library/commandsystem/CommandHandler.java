package de.minestar.library.commandsystem;

import java.util.HashMap;

import org.bukkit.command.CommandSender;

import de.minestar.minestarlibrary.utils.ChatUtils;

public class CommandHandler {

    private final String pluginName;

    public HashMap<String, AbstractCommand> registeredCommands;

    /**
     * Constructor. The given pluginname will be used in the registered commands.
     * 
     * @param pluginName
     */
    public CommandHandler(String pluginName) {
        this.pluginName = pluginName;
        this.registeredCommands = new HashMap<String, AbstractCommand>();
    }

    /**
     * Register a new command
     * 
     * @param command
     * @return <b>true</b> if the command is registered successfully, otherwise <b>false</b>
     */
    public boolean registerCommand(AbstractCommand command) {
        // set the pluginame
        command.setPluginName(pluginName);

        // CHECK: is the command already registered?
        if (this.registeredCommands.containsKey(command.getLabel())) {
            throw new RuntimeException("Command '" + command.getLabel() + "' is already registered in '" + command.getCommand() + "'!");
        }

        // register the command
        this.registeredCommands.put(command.getLabel(), command);

        // initialize the subcommands for the given command
        command.initializeSubCommands();
        return true;
    }

    /**
     * Handle a command for this CommandHandler
     */
    public boolean handleCommand(CommandSender sender, String label, String[] arguments) {
        // cast the label to lowercase
        label = label.toLowerCase();

        // in this case: the label needs to start with an '/'
        if (!label.startsWith("/")) {
            label = "/" + label;
        }

        // lookup the command
        AbstractCommand command = this.registeredCommands.get(label);
        if (command == null) {
            ChatUtils.writeError(sender, pluginName, "Command '" + label + "' registered, but not found.");
            return false;
        }

        command.handleCommand(sender, new ArgumentList(arguments));
        return true;
    }

    /**
     * List all registered commands for this CommandHandler
     */
    public void listCommands(CommandSender sender) {
        for (AbstractCommand command : this.registeredCommands.values()) {
            command.listCommand(sender);
        }
    }
}
