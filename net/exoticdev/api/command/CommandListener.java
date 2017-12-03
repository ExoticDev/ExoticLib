package net.exoticdev.api.command;

import org.bukkit.command.Command;

import java.util.List;

public abstract class CommandListener extends Command {

    protected CommandListener(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }
}