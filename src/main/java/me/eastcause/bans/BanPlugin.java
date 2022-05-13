package me.eastcause.bans;

import lombok.Getter;
import me.eastcause.bans.command.Cmd;
import me.eastcause.bans.command.PerformerManager;
import me.eastcause.bans.command.impl.GBanCommand;
import me.eastcause.bans.command.impl.GCheckBanCommand;
import me.eastcause.bans.command.impl.GUnbanCommand;
import me.eastcause.bans.configuration.Config;
import me.eastcause.bans.listeners.ConnectListeners;
import me.eastcause.bans.manager.ban.BanManager;
import me.eastcause.bans.manager.mute.MuteManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import rabbit.DataChannelManager;
import rabbit.ServerMQ;
import me.eastcause.bans.yaml.YamlManager;
import me.eastcause.bans.yaml.impl.ConfigYaml;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Arrays;

public class BanPlugin extends Plugin {

    public static void main(String[] args) {

    }

    @Getter private static BanPlugin banPlugin;
    @Getter private static ServerMQ serverMQ;

    @Override
    public void onEnable() {
        banPlugin = this;
        YamlManager.registerYaml(new ConfigYaml(this));
        Config.load();
        try {
            serverMQ = new ServerMQ(Config.getSERVERS().get("server"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        initDataChannelManager();
        BanManager.init();
        MuteManager.init();
        registerCommands(new GBanCommand(), new GUnbanCommand(), new GCheckBanCommand());
        registerListeners(new ConnectListeners());
    }

    private void initDataChannelManager() {
        DataChannelManager dataChannelManager = new DataChannelManager();
        dataChannelManager.addDataChannel("east", "performer", data -> {
            String admin = data.getOrDefault("admin", "console");
            if(admin.equalsIgnoreCase("null")){
                admin = "console";
            }
            System.out.println("admin: "+ admin);
            String cmd = data.getOrDefault("command", null);
            System.out.println("command: "+ cmd);
            if(cmd == null || cmd.equalsIgnoreCase("null")){
                return;
            }
            String[] args = data.getOrDefault("args", "test").split(" ");
            System.out.println("args: ");
            for(String arg : args){
                System.out.println(arg);
            }
            PerformerManager.execute(admin, cmd, args);
        });
        dataChannelManager.addDataChannel("east", "ban", data -> {
            String arguments = data.getOrDefault("args", null);
            String[] args = arguments.split(" ");
            if(arguments == null) return;
            String admin = data.getOrDefault("admin", "console");
            if (admin.equalsIgnoreCase("console")) {
                GBanCommand.getArgument().execute(BanPlugin.getBanPlugin().getProxy().getConsole(), args);
            }else{
                ProxiedPlayer proxiedPlayer = getProxy().getPlayer(admin);
                if(proxiedPlayer == null) {
                    GBanCommand.getArgument().execute(BanPlugin.getBanPlugin().getProxy().getConsole(), args);
                }else{
                    GBanCommand.getArgument().execute(proxiedPlayer, args);
                }
            }
        });
        /*
            dataChannelManager.addDataChannel("east", "ban", data -> {
            String arguments = data.getOrDefault("use", null);
            if(arguments == null) return;
            UUID uuid = UUID.fromString(data.getOrDefault("uuid", null));
            String name = data.getOrDefault("name", null);
            String reason = data.getOrDefault("reason", "No Reason");
            long time = TimeUtil.parseDateDiff(data.getOrDefault("time", "0"), true);
            boolean perm = Boolean.parseBoolean(data.getOrDefault("perm", "true"));
            String admin = data.getOrDefault("admin", "console");
            String srvs = data.getOrDefault("servers", "proxy");
            if(srvs == null){
                srvs = "proxy";
            }
            if(srvs.equalsIgnoreCase("null")){
                srvs = "proxy";
            }
            String[] servers = srvs.split(",");
            if (admin.equalsIgnoreCase("console")) {
                BanCommand.getArgument().execute(BanPlugin.getBanPlugin().getProxy().getConsole(), "");
            }
            BanManager.banPlayer(uuid, name, reason, time, perm, admin, true, servers);
        });
         */
    }

    private void registerCommand(Cmd cmd){
        getProxy().getPluginManager().registerCommand(this, cmd);
    }

    private void registerCommands(Cmd... cmds){
        Arrays.asList(cmds).forEach(this::registerCommand);
    }

    private void registerListener(Listener listener){
        getProxy().getPluginManager().registerListener(this, listener);
    }

    private void registerListeners(Listener... listeners){
        Arrays.asList(listeners).forEach(this::registerListener);
    }

}
