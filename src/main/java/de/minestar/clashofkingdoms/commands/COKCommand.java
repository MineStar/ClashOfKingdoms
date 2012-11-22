package de.minestar.clashofkingdoms.commands;

import de.minestar.library.commandsystem.AbstractCommand;
import de.minestar.library.commandsystem.annotations.Arguments;
import de.minestar.library.commandsystem.annotations.Description;
import de.minestar.library.commandsystem.annotations.Label;
import de.minestar.library.commandsystem.annotations.PermissionNode;

@Label(label = "/cok")
@Arguments(arguments = "")
@PermissionNode(node = "")
@Description(description = "Superkommando für Clash of Kingdoms")
public class COKCommand extends AbstractCommand {

    @Override
    protected void createSubCommands() {
        this.registerCommand(new CreateGameCommand());
        this.registerCommand(new StartGameCommand());
        this.registerCommand(new JoinGameCommand());
        this.registerCommand(new JoinTeamCommand());
        this.registerCommand(new QuitGameCommand());
    }
}
