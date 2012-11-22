package de.minestar.clashofkingdoms.commands;

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
@Arguments(arguments = "<GAMENAME>")
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
        PlayerUtils.sendSuccess(player, COKCore.NAME, "Game '" + game.getGameName() + "' created!");
    }
}
