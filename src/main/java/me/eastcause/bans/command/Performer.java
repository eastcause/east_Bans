package me.eastcause.bans.command;

import net.md_5.bungee.api.CommandSender;

@FunctionalInterface
public interface Performer {

    boolean execute(CommandSender commandSender, String[] args);

}
