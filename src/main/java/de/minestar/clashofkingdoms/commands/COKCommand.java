package de.minestar.clashofkingdoms.commands;

import org.bukkit.entity.Player;

import de.minestar.clashofkingdoms.COKCore;
import de.minestar.clashofkingdoms.data.COKGame;
import de.minestar.clashofkingdoms.enums.EnumTeam;
import de.minestar.library.commandsystem.AbstractCommand;
import de.minestar.library.commandsystem.ArgumentList;
import de.minestar.library.commandsystem.annotations.Arguments;
import de.minestar.library.commandsystem.annotations.Description;
import de.minestar.library.commandsystem.annotations.ExecuteSuperCommand;
import de.minestar.library.commandsystem.annotations.Label;
import de.minestar.library.commandsystem.annotations.PermissionNode;
import de.minestar.minestarlibrary.utils.PlayerUtils;

@Label(label = "/cok")
@Arguments(arguments = "RED|BLU|SPEC|REF")
@PermissionNode(node = "")
@Description(description = "Superkommando für Clash of Kingdoms")
@ExecuteSuperCommand
public class COKCommand extends AbstractCommand {

    @Override
    public void execute(Player player, ArgumentList argumentList) {
        COKGame game = COKCore.gameManager.getGameByPlayer(player.getName());
        if (game == null) {
            PlayerUtils.sendError(player, COKCore.NAME, "You must be in a game!");
            return;
        }

        EnumTeam team = EnumTeam.byString(argumentList.getString(0));
        game.switchTeam(player.getName(), team);
    }

    @Override
    protected void createSubCommands() {
        // players
        this.registerCommand(new CreateGameCommand());
        this.registerCommand(new CloseGameCommand());
        this.registerCommand(new StartGameCommand());
        this.registerCommand(new JoinGameCommand());
        this.registerCommand(new QuitGameCommand());

        // settings
        this.registerCommand(new SaveSettingsCommand());
        this.registerCommand(new LoadSettingsCommand());
        this.registerCommand(new SetSpawnCommand());
        this.registerCommand(new PunishTeamCommand());
        this.registerCommand(new PauseGameCommand());
        this.registerCommand(new UnpauseGameCommand());
        this.registerCommand(new StopGameCommand());
    }
}
