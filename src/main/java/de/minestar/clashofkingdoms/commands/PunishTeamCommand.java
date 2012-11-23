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

@Label(label = "punish")
@Arguments(arguments = "RED|BLU <AMOUNT>")
@PermissionNode(node = "cok.commands.punish")
@Description(description = "Punish a team")
public class PunishTeamCommand extends AbstractCommand {

    @Override
    public void execute(Player player, ArgumentList argumentList) {
        COKGame game = COKCore.gameManager.getGameByPlayer(player.getName());
        if (game == null) {
            PlayerUtils.sendError(player, COKCore.NAME, "You must be in a game!");
            return;
        }

        if (!game.getPlayer(player.getName()).isInTeam(EnumTeam.REF)) {
            PlayerUtils.sendError(player, COKCore.NAME, "You must be a referee!");
            return;
        }

        if (game.isStopped()) {
            PlayerUtils.sendError(player, COKCore.NAME, "Game is not running!");
            return;
        }

        EnumTeam team = EnumTeam.byString(argumentList.getString(0));
        game.getTeamData(team).setSpawn(player.getLocation());
        int amount = argumentList.getInt(1, 0);
        if (amount < 1) {
            PlayerUtils.sendError(player, COKCore.NAME, "The minimum is 1 block!");
            return;
        }

        game.punish(amount, team, true);
    }
}
