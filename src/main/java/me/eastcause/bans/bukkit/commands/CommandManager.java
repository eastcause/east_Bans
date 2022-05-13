package me.eastcause.bans.bukkit.commands;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.Arrays;

public class CommandManager {

    @Getter
    private CommandMap commandMap;

    public CommandManager() {
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            commandMap = (CommandMap) field.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void registerCommand(BukkitCmd bukkitCmd){
        getCommandMap().register("east", bukkitCmd);
    }

    public void registerCommands(BukkitCmd... bukkitCmds){
        Arrays.stream(bukkitCmds).forEach(this::registerCommand);
    }
}
