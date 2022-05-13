package me.eastcause.bans.utils;

import me.eastcause.bans.BanPlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Utils {

    public static String color(String s){
        if(s == null) return "";
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static boolean sendMessage(CommandSender commandSender, String message){
        if(commandSender == null) return false;
        commandSender.sendMessage(color(message));
        return true;
    }

    public static boolean sendMessage(ProxiedPlayer proxiedPlayer, String message){
        return sendMessage((CommandSender) proxiedPlayer, message);
    }
    public static boolean sendMessage(String message, String permission){
        BanPlugin.getBanPlugin().getProxy().getConsole().sendMessage(color(message));
        for(ProxiedPlayer proxiedPlayer : BanPlugin.getBanPlugin().getProxy().getPlayers()){
            if(proxiedPlayer.hasPermission(permission)){
                sendMessage(proxiedPlayer, message);
            }
        }
        return true;
    }

    public static String cancelReason(String... strings){
        StringBuilder result = new StringBuilder();
        for(String string : strings){
            result.append(string).append("\n");
        }
        if(result.length() == 0){
            return "";
        }
        return color(result.toString());
    }

}
