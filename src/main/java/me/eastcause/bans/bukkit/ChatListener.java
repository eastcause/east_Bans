package me.eastcause.bans.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import rabbit.DataMQ;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        if(event.getMessage().equalsIgnoreCase("1")){
            BanBukkitPlugin.getBanBukkitPlugin().getProxyChannel().sendData("east", "ban", new DataMQ("admin", "console"),
                    new DataMQ("args", player.getName() + " 15s chuj ci w dupe"));
        }
        if(event.getMessage().equalsIgnoreCase("2")){
            BanBukkitPlugin.getBanBukkitPlugin().getProxyChannel().sendData("east", "ban", new DataMQ("admin", "console"),
                    new DataMQ("args", player.getName() + " 15s chuj ci w dupe server=proxy"));
        }
        if(event.getMessage().equalsIgnoreCase("3")){
            BanBukkitPlugin.getBanBukkitPlugin().getProxyChannel().sendData("east", "ban", new DataMQ("admin", "console"),
                    new DataMQ("args", player.getName() + " 15s chuj ci w dupe server=spawn1"));
        }
        if(event.getMessage().equalsIgnoreCase("4")){
            BanBukkitPlugin.getBanBukkitPlugin().getProxyChannel().sendData("east", "ban", new DataMQ("admin", "console"),
                    new DataMQ("args", player.getName() + " 15s chuj ci w dupe server=spawn1,north"));
        }

        if(event.getMessage().equalsIgnoreCase("5")){
            BanBukkitPlugin.getBanBukkitPlugin().getProxyChannel().sendData("east", "ban", new DataMQ("admin", player.getName()),
                    new DataMQ("args", player.getName() + " 15s chuj ci w dupe"));
        }
        if(event.getMessage().equalsIgnoreCase("6")){
            BanBukkitPlugin.getBanBukkitPlugin().getProxyChannel().sendData("east", "ban", new DataMQ("admin", player.getName()),
                    new DataMQ("args", player.getName() + " 15s chuj ci w dupe server=proxy"));
        }
        if(event.getMessage().equalsIgnoreCase("7")){
            BanBukkitPlugin.getBanBukkitPlugin().getProxyChannel().sendData("east", "ban", new DataMQ("admin", player.getName()),
                    new DataMQ("args", player.getName() + " 15s chuj ci w dupe server=spawn1"));
        }
        if(event.getMessage().equalsIgnoreCase("8")){
            BanBukkitPlugin.getBanBukkitPlugin().getProxyChannel().sendData("east", "ban", new DataMQ("admin", player.getName()),
                    new DataMQ("args", player.getName() + " 15s chuj ci w dupe server=spawn1,north"));
        }
        /*BanBukkitPlugin.getBanBukkitPlugin().getProxyChannel().sendData("east", "ban", new DataMQ("uuid", "" + player.getUniqueId().toString()),
                new DataMQ("name", player.getName()),
                new DataMQ("reason", "Chuj ci w pysk smieciu"),
                new DataMQ("time", "30s"),
                new DataMQ("perm", "false"),
                new DataMQ("admin", "osiedlakHAze"),
                new DataMQ("servers", "spawn1,north")
                );*/
    }
}
