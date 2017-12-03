package net.exoticdev.api.command;

import org.bukkit.command.CommandSender;

public interface Command {

    void onCommand(CommandSender sender, String label, String[] args);

}