package me.eastcause.bans.bukkit.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitUtils {

    public static String color(String s){
        if(s == null) return "";
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static boolean sendMessage(CommandSender commandSender, String message){
        if(commandSender == null) return false;
        commandSender.sendMessage(color(message));
        return true;
    }

    public static boolean sendMessage(Player player, String message){
        return sendMessage((CommandSender) player, message);
    }
}
