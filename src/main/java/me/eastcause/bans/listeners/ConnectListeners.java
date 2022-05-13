package me.eastcause.bans.listeners;

import me.eastcause.bans.manager.ban.BanManager;
import me.eastcause.bans.model.Ban;
import me.eastcause.bans.utils.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class ConnectListeners implements Listener {

    @EventHandler(priority = 0)
    public void onPreLogin(PreLoginEvent event){
        if(event.isCancelled()) return;
        String name = event.getConnection().getName();
        if(BanManager.hasBan(name, "proxy")){
            Ban ban = BanManager.getBan(name, "proxy");
            if(!ban.expired()){
                event.setCancelled(true);
                event.setCancelReason(new TextComponent(BanManager.banReason(ban)));
                return;
            }
        }
    }

    @EventHandler(priority = 0)
    public void onLogin(LoginEvent event){
        UUID uuid = event.getConnection().getUniqueId();
        String name = BanManager.getName(uuid, "proxy");
        if(name == null) return;
        if(BanManager.hasBan(name, "proxy")){
            Ban ban = BanManager.getBan(name, "proxy");
            if(!ban.expired()){
                event.setCancelled(true);
                event.setCancelReason(new TextComponent(BanManager.banReason(ban)));
                return;
            }
        }
    }

    @EventHandler(priority = 0)
    public void onConnect(ServerConnectEvent event){
        if(event.isCancelled()) return;
        String name = event.getPlayer().getName();
        if(!BanManager.hasBan(name, event.getTarget().getName())){
            UUID uuid = event.getPlayer().getUniqueId();
            name = BanManager.getName(uuid, event.getTarget().getName());
        }
        if(name == null) return;
        if(BanManager.hasBan(name, event.getTarget().getName())){
            Ban ban = BanManager.getBan(name, event.getTarget().getName());
            if(!ban.expired()) {
                if (event.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)) {
                    event.getPlayer().disconnect(new TextComponent(BanManager.banReason(ban)));
                } else {
                    event.setCancelled(true);
                    for (String s : BanManager.banReason(ban).split("\n")) {
                        Utils.sendMessage(event.getPlayer(), s);
                    }
                }
            }
            return;
        }


    }
}
