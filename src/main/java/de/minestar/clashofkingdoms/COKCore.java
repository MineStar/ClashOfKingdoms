package de.minestar.clashofkingdoms;

import de.minestar.clashofkingdoms.manager.GameManager;
import de.minestar.library.PluginCore;

public class COKCore extends PluginCore {

    public static final String NAME = "Clash of Kingdoms";
    public static final String FULLNAME = "[" + NAME + "]";

    public static GameManager gameManager;

    public COKCore() {
        super(NAME);
    }

    @Override
    protected boolean createManager() {
        gameManager = new GameManager();
        return super.createManager();
    }

    @Override
    protected boolean createCommands() {
        // TODO Auto-generated method stub
        return super.createCommands();
    }

}
