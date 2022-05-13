package me.eastcause.bans.yaml;

import lombok.Getter;
import net.md_5.bungee.config.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class YamlManager {

    @Getter
    private static Map<String, Yaml> YAMLS = new HashMap<>();

    public static void registerYaml(Yaml yaml){
        getYAMLS().put(yaml.getName().toLowerCase(), yaml);
        getYAMLS().put(yaml.getName().toLowerCase().replace(".yml", ""), yaml);
    }

    public static void registerYamls(Yaml... yamls){
        Arrays.asList(yamls).forEach(YamlManager::registerYaml);
    }

    public static Yaml getYaml(String name){
        return getYAMLS().get(name.toLowerCase());
    }

    public static Configuration getConfig(String name){
        return getYAMLS().get(name.toLowerCase()).getConfiguration();
    }

    public static void reloadData(String name){
        getYAMLS().get(name.toLowerCase()).reloadData();
    }

    public static boolean saveData(String name){
        return getYAMLS().get(name.toLowerCase()).saveData();
    }

}