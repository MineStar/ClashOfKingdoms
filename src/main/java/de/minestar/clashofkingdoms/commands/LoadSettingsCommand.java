package de.minestar.clashofkingdoms.commands;

import java.io.File;

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

@Label(label = "load")
@Arguments(arguments = "<SETTINGSNAME>")
@PermissionNode(node = "cok.commands.load")
@Description(description = "Load settings")
public class LoadSettingsCommand extends AbstractCommand {

    @Override
    public void execute(Player player, ArgumentList argumentList) {
        COKGame game = COKCore.gameManager.getGameByPlayer(player.getName());
        if (game == null) {
            PlayerUtils.sendError(player, COKCore.NAME, "You are currently not in a game!");
            return;
        }

        if (!game.getPlayer(player.getName()).isInTeam(EnumTeam.REF)) {
            PlayerUtils.sendError(player, COKCore.NAME, "You are not a referee!");
            return;
        }

        if (!game.isStopped()) {
            PlayerUtils.sendError(player, COKCore.NAME, "Game is already running!");
            return;
        }

        String settingsName = argumentList.getString(0);
        File gameDir = new File(COKCore.INSTANCE.getDataFolder(), "settings");
        File thisGameDir = new File(gameDir, settingsName);
        thisGameDir.mkdir();
        File file = new File(thisGameDir, settingsName + ".dat");

        if (!file.exists()) {
            PlayerUtils.sendError(player, COKCore.NAME, "Settings  '" + settingsName + "' do not exist!");
            return;
        }
        game.getSettings().loadConfig(settingsName, game.getAllTeamData());
        PlayerUtils.sendSuccess(player, COKCore.NAME, "Settings '" + settingsName + "' loaded!");
    }
}
