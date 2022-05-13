package me.eastcause.bans.command;

import me.eastcause.bans.BanPlugin;
import me.eastcause.bans.command.impl.GBanCommand;
import me.eastcause.bans.command.impl.GCheckBanCommand;
import me.eastcause.bans.command.impl.GMuteCommand;
import me.eastcause.bans.command.impl.GUnbanCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PerformerManager {

    private static Performer getPerformer(String name){
        switch (name.toLowerCase()){
            case "ban":
                return GBanCommand.getArgument();
            case "unban":
                return GUnbanCommand.getArgument();
            case "checkban":
                return GCheckBanCommand.getArgument();
            case "mute":
                return GMuteCommand.getArgument();
            default:
                return null;
        }
    }

    public static boolean execute(String admin, String cmd, String[] args){
        Performer argument = getPerformer(cmd);
        if(argument == null) return false;
        CommandSender commandSender = BanPlugin.getBanPlugin().getProxy().getConsole();
        if(admin == null || admin.equalsIgnoreCase("console")){
            return argument.execute(commandSender, args);
        }
        ProxiedPlayer proxiedPlayer = BanPlugin.getBanPlugin().getProxy().getPlayer(admin);
        if(proxiedPlayer != null){
            return argument.execute(proxiedPlayer, args);
        }
        return argument.execute(commandSender, args);
    }
}
