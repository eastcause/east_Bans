package me.eastcause.bans.configuration;

import lombok.Getter;
import me.eastcause.bans.yaml.YamlManager;
import net.md_5.bungee.config.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

import rabbit.*;

public class Config {

    @Getter private static Map<String, Server> SERVERS = new LinkedHashMap<>();

    public static void load(){
        Configuration configuration = YamlManager.getConfig("settings.yml");
        System.out.println(configuration);
        for(String server : configuration.getSection("rabbitmq").getKeys()){
            String host = configuration.getString("rabbitmq."+server+".host");
            int port = configuration.getInt("rabbitmq."+server+".port");
            String queue = configuration.getString("rabbitmq."+server+".queue");
            getSERVERS().put(server.toLowerCase(), new Server(server, host, port, queue));
        }
        System.out.println(SERVERS.toString());
    }
}
