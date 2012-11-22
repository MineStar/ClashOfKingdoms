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

@Label(label = "quit")
@Arguments(arguments = "")
@PermissionNode(node = "cok.commands.quit")
@Description(description = "Leave a game")
public class QuitGameCommand extends AbstractCommand {

    @Override
    public void execute(Player player, ArgumentList argumentList) {
        COKGame game = COKCore.gameManager.getGameByPlayer(player.getName());
        if (game == null) {
            PlayerUtils.sendError(player, COKCore.NAME, "You are currently not in a game!");
            return;
        }

        if (game.playerQuitGame(player.getName())) {
            PlayerUtils.sendSuccess(player, COKCore.NAME, "You  have left the game!");
        }
    }
}
