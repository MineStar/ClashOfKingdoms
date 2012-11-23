package de.minestar.clashofkingdoms.commands;

import org.bukkit.ChatColor;
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

@Label(label = "setspawn")
@Arguments(arguments = "RED|BLU|REF|NONE")
@PermissionNode(node = "cok.commands.setspawn")
@Description(description = "Set a spawn")
public class SetSpawnCommand extends AbstractCommand {

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

        if (game.isRunning()) {
            PlayerUtils.sendError(player, COKCore.NAME, "Game is already running!");
            return;
        }

        EnumTeam team = EnumTeam.byString(argumentList.getString(0));
        game.getTeamData(team).setSpawn(player.getLocation());

        PlayerUtils.sendSuccess(player, COKCore.NAME, "Spawn for " + team.getFullTeamName(ChatColor.GREEN) + " set!");
    }
}
