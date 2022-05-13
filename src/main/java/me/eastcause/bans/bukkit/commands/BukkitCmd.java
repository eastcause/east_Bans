package me.eastcause.bans.bukkit.commands;

import me.eastcause.bans.bukkit.utils.BukkitUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;

public abstract class BukkitCmd extends Command {
    public BukkitCmd(String name) {
        super(name);
        setPermission("east.bans.command." + name);
        setPermissionMessage("&cYou dont have access: &7" + getPermission());
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (getPermission() != null && !hasPermission(commandSender)) {
            return sendMessageNoPermission(commandSender);
        }
        if(commandSender instanceof RemoteConsoleCommandSender){
            return run((RemoteConsoleCommandSender)commandSender, strings);
        }
        return run(commandSender, strings);
    }

    public abstract boolean run(CommandSender commandSender, String[] args);

    public boolean hasPermission(CommandSender commandSender){
        if(commandSender.hasPermission(getPermission())){
            return true;
        }
        return false;
    }

    public boolean sendMessageNoPermission(CommandSender commandSender){
        return BukkitUtils.sendMessage(commandSender, getPermissionMessage());
    }
}
