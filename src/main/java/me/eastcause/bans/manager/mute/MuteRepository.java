package me.eastcause.bans.manager.mute;

import lombok.Getter;
import me.eastcause.bans.BanPlugin;
import me.eastcause.bans.manager.ban.BanManager;
import me.eastcause.bans.manager.ban.BanRepositoryIO;
import me.eastcause.bans.model.Ban;
import me.eastcause.bans.model.Mute;
import me.eastcause.bans.utils.Utils;
import me.eastcause.bans.utils.time.TimeUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MuteRepository implements MuteRepositoryIO {

    @Getter private String server;
    @Getter public ConcurrentHashMap<String, Mute> MUTES;
    @Getter public ConcurrentHashMap<UUID, String> NAMES;

    public MuteRepository(String server){
        this.server = server.toLowerCase();
        this.MUTES = new ConcurrentHashMap<>();
        this.NAMES = new ConcurrentHashMap<>();
    }

    @Override
    public boolean mutePlayer(UUID uuid, String name, String reason, long time, boolean perm, String admin, boolean ignoreBan) {
        Mute mute = new Mute(uuid, name, reason, time, perm, server, admin);
        ProxiedPlayer proxiedPlayer = null;
        if(uuid != null){
            proxiedPlayer = BanPlugin.getBanPlugin().getProxy().getPlayer(uuid);
        }
        if(proxiedPlayer == null){
            proxiedPlayer = BanPlugin.getBanPlugin().getProxy().getPlayer(name);
        }
        if(proxiedPlayer != null){
            /*if(server.equalsIgnoreCase("proxy")) {
                Utils.sendMessage(proxiedPlayer, "&bYou have been &6"+(mute.isPerm() ? "permanently" : "temporarily")+" &b"+(mute.isPerm() ? "" :"for: &6" +TimeUtil.longToStringTime(mute.getTime()) + " " )+"&bmutated by: &6"+mute.getAdmin()+" &bfor: &6"+mute.getReason()+" &bon server: &6" + mute.getServer());
            }else if(server.equalsIgnoreCase(proxiedPlayer.getServer().getInfo().getName())){
                BanManager.kickBan(proxiedPlayer, mute);
            }*/
        }
        if(ignoreBan || !getMUTES().containsKey(name.toLowerCase())){
            return putMute(mute);
        }
        return false;
    }

    @Override
    public boolean putMute(Mute mute) {
        getMUTES().put(mute.getName().toLowerCase(), mute);
        if(mute.getUuid() != null){
            getNAMES().put(mute.getUuid(), mute.getName().toLowerCase());
        }
        return true;
    }

    @Override
    public boolean unMute(Mute mute) {
        if(mute == null) {
            return false;
        }
        getMUTES().remove(mute.getName().toLowerCase());
        if(mute.getUuid() != null){
            getNAMES().remove(mute.getUuid());
        }
        return true;
    }

    @Override
    public Mute getMute(String name) {
        return getMUTES().getOrDefault(name.toLowerCase(), null);
    }

    @Override
    public Mute getMute(UUID uuid) {
        String name = getNAMES().getOrDefault(uuid, null);
        if(name == null) return null;
        return getMUTES().get(name.toLowerCase());
    }

    @Override
    public boolean hasMute(String name) {
        return (getMute(name.toLowerCase()) != null);
    }

    @Override
    public boolean muteExpired(String name) {
        Mute mute = getMute(name.toLowerCase());
        return (mute != null && (mute.getTime() != 0L && (mute.getTime() <= System.currentTimeMillis())));
    }

}
