package net.exoticdev.api.command;

import net.exoticdev.api.reflection.ReflectionTools;
import net.exoticdev.api.spigot.Spigot;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandTools {

    private static CommandTools tools;

    public static CommandTools getInstance() {
        if(CommandTools.tools == null) {
            CommandTools.tools = new CommandTools();
        }

        return CommandTools.tools;
    }

    private List<Command> commands = new ArrayList<>();
    private Map<Class, List<ChainedCommand>> chainedCommands = new HashMap<>();

    public void regCommand(Command command, String[] aliases) {
        try {
            CommandID id = (CommandID) ReflectionTools.getAnnotation(command.getClass().getMethod("onCommand", CommandSender.class, String.class, String[].class), CommandID.class);

            SimpleCommandMap commandMap = (SimpleCommandMap) ReflectionTools.getFieldValue(Spigot.getServer(), "commandMap");

            aliases = ArrayUtils.addAll(aliases, id.aliases());

            commandMap.register(id.name().toLowerCase(), new CommandListener(id.name().toLowerCase(), id.description(), "", Arrays.asList(aliases)) {

                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    for(Command command : CommandTools.this.commands) {
                        try {
                            CommandID id = (CommandID) ReflectionTools.getAnnotation(command.getClass().getDeclaredMethod("onCommand", CommandSender.class, String.class, String[].class), CommandID.class);

                            if(this.getName().equalsIgnoreCase(id.name())) {
                                if(id.allowConsole() || sender instanceof Player) {
                                    command.onCommand(sender, label, args);
                                } else {
                                    sender.sendMessage("§cYou need to be a player to use this command.");
                                }
                            }
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }

                    return true;
                }
            });

            this.commands.add(command);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void regCommand(Command command) {
        this.regCommand(command, new String[0]);
    }

    public void regChainedCommand(ChainedCommand command) {
        try {
            Class base = command.getCommand();

            ChainedCommandBase baseObject = (ChainedCommandBase) base.newInstance();
            ChainedCommandBaseID baseID = (ChainedCommandBaseID) ReflectionTools.getAnnotation(command.getCommand().newInstance().getClass(), ChainedCommandBaseID.class);

            SimpleCommandMap commandMap = (SimpleCommandMap) ReflectionTools.getFieldValue(Spigot.getServer(), "commandMap");

            commandMap.register(baseID.name().toLowerCase(), new CommandListener(baseID.name().toLowerCase(), baseID.description(), "", Arrays.asList(baseID.aliases())) {

                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    for(Map.Entry<Class, List<ChainedCommand>> commands : CommandTools.this.chainedCommands.entrySet()) {
                        try {
                            ChainedCommandBaseID baseID = (ChainedCommandBaseID) ReflectionTools.getAnnotation(command.getCommand().newInstance().getClass(), ChainedCommandBaseID.class);

                            if(!commands.getKey().equals(base)) {
                                continue;
                            }

                            if(this.getName().equals(baseID.name())) {
                                if(baseID.allowConsole() || sender instanceof Player) {
                                    boolean foundArgument = false;

                                    String argument = null;

                                    if(args.length != 0) {
                                        argument = args[0];
                                    }

                                    if(argument != null) {
                                        for(ChainedCommand chainedCommand : commands.getValue()) {
                                            ChainedCommandID id = (ChainedCommandID) ReflectionTools.getAnnotation(chainedCommand.getClass().getMethod("onCommand", CommandSender.class, String.class, String[].class), ChainedCommandID.class);

                                            if(args.length >= id.minimumArguments()) {
                                                if(args.length != 0) {
                                                    for(int i = 0; i < id.arguments().length; i++) {
                                                        if(argument.equalsIgnoreCase(id.arguments()[i])) {
                                                            chainedCommand.onCommand(sender, argument, args);

                                                            foundArgument = true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if(!foundArgument) {
                                        if(argument != null) {
                                            boolean breakLoop = false;

                                            for(ChainedCommand chainedCommand : commands.getValue()) {
                                                if(breakLoop) {
                                                    continue;
                                                }

                                                ChainedCommandID id = (ChainedCommandID) ReflectionTools.getAnnotation(chainedCommand.getClass().getMethod("onCommand", CommandSender.class, String.class, String[].class), ChainedCommandID.class);

                                                for(int i = 0; i < id.arguments().length; i++) {
                                                    if(argument.equalsIgnoreCase(id.arguments()[i])) {
                                                        sender.sendMessage(command.getSyntax());

                                                        breakLoop = true;
                                                    }
                                                }
                                            }

                                            if(!breakLoop) {
                                                sender.sendMessage(baseObject.getHelpMessage());
                                            }
                                        } else {
                                            sender.sendMessage(baseObject.getHelpMessage());
                                        }
                                    }
                                } else {
                                    sender.sendMessage("§cYou need to be a player to use this command.");
                                }
                            }
                        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }

                    return true;
                }
            });

            List<ChainedCommand> chainedCommandList = this.chainedCommands.get(base);

            if(chainedCommandList == null) {
                chainedCommandList = new ArrayList<>();
            }

            chainedCommandList.add(command);

            this.chainedCommands.put(base, chainedCommandList);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}