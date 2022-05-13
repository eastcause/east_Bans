package me.eastcause.bans.bukkit.commands.impl;

import me.eastcause.bans.bukkit.BanBukkitPlugin;
import me.eastcause.bans.bukkit.commands.BukkitCmd;
import me.eastcause.bans.bukkit.utils.BukkitUtils;
import org.bukkit.command.CommandSender;
import rabbit.DataMQ;

public class UnbanCommand extends BukkitCmd {
    public UnbanCommand() {
        super("unban");
        setDescription("Unban player");
        setUsage("/unban <nick> <reason> <server ? server=serverName>");
    }

    @Override
    public boolean run(CommandSender commandSender, String[] args) {
        if(args.length == 0){
            return BukkitUtils.sendMessage(commandSender, "&cUsage: &7" + getUsage());
        }
        StringBuilder arg = new StringBuilder();
        for(String a : args){
            arg.append(a).append(" ");
        }
        arg = new StringBuilder(arg.substring(0, arg.length() - 1));
        BanBukkitPlugin.getBanBukkitPlugin().getProxyChannel().sendData("east", "performer",
                new DataMQ("admin", commandSender.getName()),
                new DataMQ("command", "unban"),
                new DataMQ("args", "" + arg));
        return false;
    }
}
