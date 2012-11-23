package de.minestar.clashofkingdoms.commands;

import org.bukkit.entity.Player;

import de.minestar.clashofkingdoms.COKCore;
import de.minestar.clashofkingdoms.data.COKGame;
import de.minestar.clashofkingdoms.enums.EnumTeam;
import de.minestar.library.commandsystem.AbstractCommand;
import de.minestar.library.commandsystem.ArgumentList;
import de.minestar.library.commandsystem.annotations.Arguments;
import de.minestar.library.commandsystem.annotations.Description;
import de.minestar.library.commandsystem.annotations.Label;
import de.minestar.library.commandsystem.annotations.PermissionNode;
import de.minestar.minestarlibrary.utils.PlayerUtils;

@Label(label = "join")
@Arguments(arguments = "<GAMENAME> RED|BLU|REF|NONE")
@PermissionNode(node = "cok.commands.join")
@Description(description = "Join a game")
public class JoinGameCommand extends AbstractCommand {

    @Override
    public void execute(Player player, ArgumentList argumentList) {
        if (COKCore.gameManager.isPlayerInAnyGame(player.getName())) {
            PlayerUtils.sendError(player, COKCore.NAME, "You are already in a game!");
            return;
        }

        COKGame game = COKCore.gameManager.getGame(argumentList.getString(0));
        if (game == null) {
            PlayerUtils.sendError(player, COKCore.NAME, "Game '" + argumentList.getString(0) + "' not found!");
            return;
        }

        EnumTeam team = EnumTeam.byString(argumentList.getString(1));
        game.playerJoinGame(player.getName(), team);
    }
}
