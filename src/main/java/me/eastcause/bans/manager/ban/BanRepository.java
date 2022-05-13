package me.eastcause.bans.manager.ban;

import lombok.Getter;
import me.eastcause.bans.BanPlugin;
import me.eastcause.bans.model.Ban;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BanRepository implements BanRepositoryIO{

    @Getter private String server;
    @Getter public ConcurrentHashMap<String, Ban> BANS;
    @Getter public ConcurrentHashMap<UUID, String> NAMES;

    public BanRepository(String server){
        this.server = server.toLowerCase();
        this.BANS = new ConcurrentHashMap<>();
        this.NAMES = new ConcurrentHashMap<>();
    }

    @Override
    public boolean banPlayer(UUID uuid, String name, String reason, long time, boolean perm, String admin, boolean ignoreBan) {
        Ban ban = new Ban(uuid, name, reason, time, perm, server, admin);
        ProxiedPlayer proxiedPlayer = null;
        if(uuid != null){
            proxiedPlayer = BanPlugin.getBanPlugin().getProxy().getPlayer(uuid);
        }
        if(proxiedPlayer == null){
            proxiedPlayer = BanPlugin.getBanPlugin().getProxy().getPlayer(name);
        }
        if(proxiedPlayer != null){
            if(server.equalsIgnoreCase("proxy")) {
                BanManager.kickBan(proxiedPlayer, ban);
            }else if(server.equalsIgnoreCase(proxiedPlayer.getServer().getInfo().getName())){
                BanManager.kickBan(proxiedPlayer, ban);
            }
        }
        if(ignoreBan || !getBANS().containsKey(name.toLowerCase())){
            return putBan(ban);
        }
        return false;
    }

    @Override
    public boolean putBan(Ban ban) {
        getBANS().put(ban.getName().toLowerCase(), ban);
        if(ban.getUuid() != null){
            getNAMES().put(ban.getUuid(), ban.getName().toLowerCase());
        }
        return true;
    }

    @Override
    public boolean unban(Ban ban) {
        if(ban == null) {
            return false;
        }
        getBANS().remove(ban.getName().toLowerCase());
        if(ban.getUuid() != null){
            getNAMES().remove(ban.getUuid());
        }
        return true;
    }

    @Override
    public Ban getBan(String name) {
        return getBANS().getOrDefault(name.toLowerCase(), null);
    }

    @Override
    public Ban getBan(UUID uuid) {
        String name = getNAMES().getOrDefault(uuid, null);
        if(name == null) return null;
        return getBANS().get(name.toLowerCase());
    }

    @Override
    public boolean hasBan(String name) {
        return (getBan(name.toLowerCase()) != null);
    }

    @Override
    public boolean banExpired(String name) {
        Ban ban = getBan(name.toLowerCase());
        return (ban != null && (ban.getTime() != 0L && (ban.getTime() <= System.currentTimeMillis())));
    }

}
