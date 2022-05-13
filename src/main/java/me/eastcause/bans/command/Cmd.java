package me.eastcause.bans.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public abstract class Cmd extends Command {

    public Cmd(String name, String... aliases) {
        super(name, "east.bans.command." + name, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args){
        onCommand(sender, args);
    }

    public abstract boolean onCommand(CommandSender sender, String[] args);

}
