package de.minestar.library;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import de.minestar.library.commandsystem.AbstractCommand;
import de.minestar.library.commandsystem.CommandHandler;
import de.minestar.minestarlibrary.annotations.UseStatistic;
import de.minestar.minestarlibrary.utils.ConsoleUtils;

public class PluginCore extends JavaPlugin {
    private CommandHandler commandHandler;
    private String tempName;

    public PluginCore() {
        this("");
    }

    public PluginCore(String name) {
        tempName = name;
    }

    @Override
    public final void onDisable() {
        if (!commonDisable()) {
            ConsoleUtils.printError(tempName, "Can't handle common disable action! Possible data loss!");
        }
        ConsoleUtils.printInfo(tempName, "Disabled v" + getDescription().getVersion() + "!");
    }

    @Override
    public final void onEnable() {

        // create plugins datafolder
        getDataFolder().mkdirs();

        // LOAD CONFIGS
        if (!loadingConfigs(getDataFolder())) {
            ConsoleUtils.printError(tempName, "Can't load configuration! Plugin is not enabled!");
            this.setEnabled(false);
            return;
        }

        // CREATE MANAGER
        if (!createManager()) {
            ConsoleUtils.printError(tempName, "Can't create manager! Plugin is not enabled!");
            this.setEnabled(false);
            return;
        }

        // CREATE LISTENER
        if (!createListener()) {
            ConsoleUtils.printError(tempName, "Can't create listener! Plugin is not enabled!");
            this.setEnabled(false);
            return;
        }

        // CREATE COMMANDS
        try {
            this.commandHandler = new CommandHandler(tempName);
            if (!this.createCommands()) {
                ConsoleUtils.printError(tempName, "Can't create commands! Plugin is not enabled!");
                this.setEnabled(false);
                return;
            }
        } catch (Exception e) {
            ConsoleUtils.printException(e, tempName, e.getMessage());
            this.setEnabled(false);
            return;
        }

        // REGISTER THE EVENTS FROM LISTENER
        if (!registerEvents(getServer().getPluginManager())) {
            ConsoleUtils.printError(tempName, "Can't register events! Plugin is not enabled!");
            this.setEnabled(false);
            return;
        }

        // CREATE RUNNABLES
        if (!createThreads()) {
            ConsoleUtils.printError(tempName, "Can't create threads! Plugin is not enabled!");
            this.setEnabled(false);
            return;
        }

        // START THE RUNNABLES
        if (!startThreads(getServer().getScheduler())) {
            ConsoleUtils.printError(tempName, "Can't start threads! Plugin is not enabled!");
            this.setEnabled(false);
            return;
        }

        // DO WE USE STATISTICS? SEE ANNOTATIONS OF THE CLASS....
        // "Mels Idee!!!" "GeMos Umsetzung :P"
        if (this.usesStatistics())
            handleRegisterStatistic();

        // OTHER THINGS ON ENABLE
        if (!commonEnable()) {
            ConsoleUtils.printError(tempName, "Can't initiate common things! Plugin is not enabled!");
            this.setEnabled(false);
            return;
        }

        ConsoleUtils.printInfo(tempName, "Enabled v" + getDescription().getVersion() + " !");
    }
    /**
     * Loading configuration files or creating default ones
     * 
     * @param dataFolder
     *            The plugins data folder
     * @return <code>True</code> when it was sucessfull without errors
     */
    protected boolean loadingConfigs(File dataFolder) {
        return true;
    }

    /**
     * Creating the manager and handler of the plugin
     * 
     * @return <code>True</code> when it was sucessfull without errors
     */
    protected boolean createManager() {
        return true;
    }

    /**
     * Creating the listers for the plugin
     * 
     * @return <code>True</code> when it was sucessfull without errors
     */
    protected boolean createListener() {
        return true;
    }

    /**
     * Creating the commands of the plugin
     * 
     * @return <code>True</code> when it was sucessfull without errors
     */
    protected boolean createCommands() {
        return true;
    }

    /**
     * Register the created listers to the plugin
     * 
     * @return <code>True</code> when it was sucessfull without errors
     */
    protected boolean registerEvents(PluginManager pm) {
        return true;
    }

    /**
     * Creating the threads running in the plugin
     * 
     * @return <code>True</code> when it was sucessfull without errors
     */
    protected boolean createThreads() {
        return true;
    }

    /**
     * Start the threads running in the plugin
     * 
     * @return <code>True</code> when it was sucessfull without errors
     */
    protected boolean startThreads(BukkitScheduler scheduler) {
        return true;
    }

    /**
     * Register statistics for the Illuminati plugin. Will throw an exception when the plugin is not found
     * 
     * @return <code>True</code> when it was sucessfull without errors
     */
    protected boolean registerStatistics() {
        return true;
    }

    /**
     * Handle common things on plugin enable
     * 
     * @return <code>True</code> when it was sucessfull without errors
     */
    protected boolean commonEnable() {
        return true;
    }

    /**
     * Does this Class have the annotation {@link UseStatistic} ?
     * 
     * @return <b>true</b> if yes, otherwise <b>false</b>
     */
    private boolean usesStatistics() {
        return this.getClass().isAnnotationPresent(UseStatistic.class);
    }

    /**
     * Searching first for the Plugin "Illuminati" and check whether it is enabled. When the plugin is enabled it will call the overriden methode
     */
    private void handleRegisterStatistic() {
        // SEARCH FOR ILLUMINATI
        Plugin p = getServer().getPluginManager().getPlugin("Illuminati");
        boolean illuminatiEnabled = p != null && p.isEnabled();

        // REGISTER STATISTICS TO ILLUMINATI
        if (illuminatiEnabled) {
            if (!registerStatistics())
                ConsoleUtils.printError(tempName, "Can't register statistics! Statistics are disabled!");
            else
                ConsoleUtils.printInfo(tempName, "Statistics are registered!");

        } else
            ConsoleUtils.printWarning(tempName, "Illuminati was not found. Can't register statistics!");

    }

    /**
     * Handle common things on plugin disable
     * 
     * @return <code>True</code> when it was sucessfull without errors
     */
    protected boolean commonDisable() {
        return true;
    }

    /**
     * Method to list all commands
     * 
     * @param sender
     */
    public final void listCommands(ConsoleCommandSender sender) {
        this.commandHandler.listCommands(Bukkit.getConsoleSender());
    }

    /**
     * Register a command for this plugin
     * 
     * @param command
     */
    protected final void registerCommand(AbstractCommand command) {
        try {
            commandHandler.registerCommand(command);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Handle commands
     */
    @Override
    public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return this.commandHandler.handleCommand(sender, label, args);
    }
}
