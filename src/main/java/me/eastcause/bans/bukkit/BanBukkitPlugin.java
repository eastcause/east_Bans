package me.eastcause.bans.bukkit;

import lombok.Getter;
import me.eastcause.bans.bukkit.commands.CommandManager;
import me.eastcause.bans.bukkit.commands.impl.BanCommand;
import me.eastcause.bans.bukkit.commands.impl.CheckBanCommand;
import me.eastcause.bans.bukkit.commands.impl.UnbanCommand;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import rabbit.ChannelMQ;
import rabbit.DataChannelManager;
import rabbit.Server;
import rabbit.ServerMQ;

import java.util.Arrays;
import java.util.Map;

public class BanBukkitPlugin extends JavaPlugin {

    @Getter private static BanBukkitPlugin banBukkitPlugin;
    @Getter private Server srv;
    @Getter private Server proxy;
    @Getter private ServerMQ serverMQ;
    @Getter private ChannelMQ proxyChannel;

    @Override
    public void onEnable() {
        banBukkitPlugin = this;
        saveDefaultConfig();
        loadServers();
        try {
            serverMQ = new ServerMQ(srv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initDataChannelManager();
        proxyChannel = new ChannelMQ(proxy);
        registerListener(new ChatListener());
        new CommandManager().registerCommands(new BanCommand(), new CheckBanCommand(), new UnbanCommand());
    }

    public void loadServers(){
        srv = new Server(
                "server",
                getConfig().getString("rabbitmq.server.host"),
                getConfig().getInt("rabbitmq.server.port"),
                getConfig().getString("rabbitmq.server.queue")
        );
        proxy = new Server(
                "proxy",
                getConfig().getString("rabbitmq.proxy.host"),
                getConfig().getInt("rabbitmq.proxy.port"),
                getConfig().getString("rabbitmq.proxy.queue")
        );
    }

    private void initDataChannelManager() {
        DataChannelManager dataChannelManager = new DataChannelManager();
        dataChannelManager.addDataChannel("connection", "test", data -> {
            System.out.println(" [*] Received `connection#test` Connection DataChannel:");
            for(Map.Entry<String, String> kv : data.entrySet()){
                System.out.println("  " + kv.getKey() + " - " + kv.getValue());
            }
        });
    }

    private void registerListener(Listener listener){
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    private void registerListeners(Listener... listeners){
        Arrays.asList(listeners).forEach(this::registerListener);
    }
}
