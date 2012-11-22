package de.minestar.clashofkingdoms;

import org.bukkit.plugin.PluginManager;

import de.minestar.clashofkingdoms.commands.COKCommand;
import de.minestar.clashofkingdoms.listener.AdminListener;
import de.minestar.clashofkingdoms.listener.GameListener;
import de.minestar.clashofkingdoms.manager.GameManager;
import de.minestar.library.PluginCore;

public class COKCore extends PluginCore {

    public static final String NAME = "Clash of Kingdoms";
    public static final String FULLNAME = "[" + NAME + "]";

    public static GameManager gameManager;
    public static GameListener gameListener;
    public static AdminListener adminListener;

    public COKCore() {
        super(NAME);
    }

    @Override
    protected boolean createManager() {
        gameManager = new GameManager();
        adminListener = new AdminListener();
        gameListener = new GameListener();
        return super.createManager();
    }

    @Override
    protected boolean registerEvents(PluginManager pm) {
        pm.registerEvents(adminListener, this);
        pm.registerEvents(gameListener, this);
        return super.registerEvents(pm);
    }

    @Override
    protected boolean createCommands() {
        this.registerCommand(new COKCommand());
        return super.createCommands();
    }

}
