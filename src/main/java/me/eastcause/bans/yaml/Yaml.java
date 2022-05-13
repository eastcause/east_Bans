package me.eastcause.bans.yaml;

import lombok.Data;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Data
public abstract class Yaml implements Wad{

    private String name;
    private Plugin plugin;
    private File file;
    private Configuration configuration;

    public Yaml(String name, Plugin plugin){
        this.name = name;
        this.plugin = plugin;
        checkFiles();
        topUp();
        saveData();
    }


    @Override
    public boolean checkFiles() {
        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdir();
        file = new File(plugin.getDataFolder(), name);
        if (!file.exists()){
            try {
                Files.copy(plugin.getResourceAsStream(name), file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        boolean load = reloadData();
        if(!load) return false;
        return true;
    }

    @Override
    public boolean saveData() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(plugin.getDataFolder(), name));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean reloadData() {
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), name));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean setIfThereIsNo(String path, Object object){
        Object o = configuration.get(path);
        if(o == null){
            configuration.set(path, object);
            return true;
        }
        return false;
    }
}
