package de.minestar.clashofkingdoms.commands;

import java.io.File;

import org.bukkit.entity.Player;

import de.minestar.clashofkingdoms.COKCore;
import de.minestar.clashofkingdoms.data.COKGame;
import de.minestar.library.commandsystem.AbstractCommand;
import de.minestar.library.commandsystem.ArgumentList;
import de.minestar.library.commandsystem.annotations.Arguments;
import de.minestar.library.commandsystem.annotations.Description;
import de.minestar.library.commandsystem.annotations.Label;
import de.minestar.library.commandsystem.annotations.PermissionNode;
import de.minestar.minestarlibrary.utils.PlayerUtils;

@Label(label = "create")
@Arguments(arguments = "<GAMENAME> [<SETTINGS>]")
@PermissionNode(node = "cok.commands.create")
@Description(description = "Create a game")
public class CreateGameCommand extends AbstractCommand {

    @Override
    public void execute(Player player, ArgumentList argumentList) {
        COKGame game = COKCore.gameManager.createGame(argumentList.getString(0));
        if (game == null) {
            PlayerUtils.sendError(player, COKCore.NAME, "Game '" + argumentList.getString(0) + "' already exists!");
            return;
        }

        if (argumentList.length() > 1) {
            String settingsName = argumentList.getString(1);
            File gameDir = new File(COKCore.INSTANCE.getDataFolder(), "settings");
            File thisGameDir = new File(gameDir, settingsName);
            thisGameDir.mkdir();
            File file = new File(thisGameDir, settingsName + ".dat");

            if (!file.exists()) {
                PlayerUtils.sendError(player, COKCore.NAME, "Settings '" + settingsName + "' do not exist!");
                return;
            }
            game.getSettings().loadConfig(settingsName, game.getAllTeamData());
        }

        PlayerUtils.sendSuccess(player, COKCore.NAME, "Game '" + game.getGameName() + "' created!");
    }
}
