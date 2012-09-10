package de.minestar.clashofkingdoms;

import de.minestar.clashofkingdoms.manager.GameManager;
import de.minestar.minestarlibrary.AbstractCore;

public class COKCore extends AbstractCore {

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
}
