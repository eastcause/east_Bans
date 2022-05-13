package me.eastcause.bans.yaml.impl;

import me.eastcause.bans.yaml.Yaml;
import net.md_5.bungee.api.plugin.Plugin;

public class ConfigYaml extends Yaml {

    public ConfigYaml(Plugin plugin) {
        super("settings.yml", plugin);
    }

    @Override
    public void topUp() {
        setIfThereIsNo("rabbitmq.server.host", "127.0.0.1");
        setIfThereIsNo("rabbitmq.server.port", 5672);
        setIfThereIsNo("rabbitmq.server.host", "bungeeBansMQ");
    }
}
