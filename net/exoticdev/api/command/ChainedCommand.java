package net.exoticdev.api.command;

import org.bukkit.command.CommandSender;

public abstract class ChainedCommand {

    private Class<? extends ChainedCommandBase> command;

    public ChainedCommand(Class<? extends ChainedCommandBase> command) {
        this.command = command;
    }

    public Class<? extends ChainedCommandBase> getCommand() {
        return this.command;
    }

    public abstract void onCommand(CommandSender sender, String argument, String[] args);

    public abstract String getSyntax();

}