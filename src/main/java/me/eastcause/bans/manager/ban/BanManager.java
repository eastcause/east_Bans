package me.eastcause.bans.manager.ban;

import lombok.Getter;
import me.eastcause.bans.configuration.Config;
import me.eastcause.bans.model.Ban;
import me.eastcause.bans.utils.Utils;
import me.eastcause.bans.utils.time.TimeUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import rabbit.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BanManager {

    /**
     * @param key - server name
     * @param value - BanRepository
     */
    @Getter private static ConcurrentHashMap<String, BanRepository> REPOSITORIES = new ConcurrentHashMap<>();

    public static void createRepository(String server){
        getREPOSITORIES().put(server.toLowerCase(), new BanRepository(server));
    }

    public static BanRepository getRepositoryByServer(String server){
        return getREPOSITORIES().getOrDefault(server.toLowerCase(), null);
    }

    public static void init(){
        createRepository("proxy");
        for(Server server : Config.getSERVERS().values()){
            if(server.getServer().equalsIgnoreCase("server")) continue;
            createRepository(server.getServer());
        }
    }

    /**
     * @param servers If u want bon a player for all servers, u can type `proxy`.
     */

    public static boolean banPlayer(UUID uuid, String name, String reason, long time, boolean perm, String admin, boolean ignoreBan, String... servers){
        boolean proxy = (typedProxy(servers));
        if(proxy || getREPOSITORIES().size() == 1){
            BanRepository banRepository = getRepositoryByServer("proxy");
            banRepository.banPlayer(uuid, name, reason, time, perm, admin, ignoreBan);
            return true;
        }
        if(servers.length == 1){
            BanRepository banRepository = getRepositoryByServer(servers[0]);
            if(banRepository == null){
                return false;
            }
        }
        for(String server : servers){
            BanRepository banRepository = getRepositoryByServer(server);
            if(banRepository == null){
                System.err.println("BanRepository from server: "+server+" does not exist!");
                continue;
            }
            banRepository.banPlayer(uuid, name, reason, time, perm, admin, ignoreBan);
        }
        return true;
    }

    public static boolean unbanPlayer(String name, String... servers){
        boolean proxy = typedProxy(servers);
        if(proxy || getREPOSITORIES().size() == 1){
            BanRepository banRepository = getRepositoryByServer("proxy");
            if(!banRepository.hasBan(name)){
                return false;
            }
            banRepository.unban(banRepository.getBan(name));
            return true;
        }
        if(servers.length == 1){
            BanRepository banRepository = getRepositoryByServer(servers[0]);
            if(banRepository == null){
                return false;
            }
        }
        for(String server : servers){
            BanRepository banRepository =getRepositoryByServer(server);
            if(banRepository == null){
                System.err.println("BanRepository from server: "+server+" does not exist!");
                continue;
            }
            if(!banRepository.hasBan(name)){
                continue;
            }
            banRepository.unban(banRepository.getBan(name));
        }
        return true;
    }

    public static boolean hasBan(String name, String server){
        BanRepository banRepository = getRepositoryByServer(server);
        if(banRepository == null) return false;
        return banRepository.hasBan(name);
    }

    public static boolean hasBan(String name, BanRepository banRepository){
        if(banRepository == null) return false;
        return banRepository.hasBan(name);
    }

    public static Ban getBan(String name, String server){
        BanRepository banRepository = getRepositoryByServer(server);
        if(banRepository == null) return null;
        return (hasBan(name, banRepository) ? banRepository.getBan(name) : null);
    }

    public static List<Ban> getBans(String name){
        List<Ban> bans = new ArrayList<>();
        for(BanRepository banRepository : getREPOSITORIES().values()){
            if(hasBan(name, banRepository)){
                bans.add(banRepository.getBan(name));
            }
        }
        return bans;
    }

    public static String getName(UUID uuid, String server){
        BanRepository banRepository = getRepositoryByServer(server);
        if(banRepository == null) return null;
        Ban ban = banRepository.getBan(uuid);
        return (ban != null ? ban.getName() : null);
    }

    private static boolean typedProxy(String... servers) {
        if (servers.length == 0) return true;
        return Arrays.stream(servers).anyMatch(s -> s.equalsIgnoreCase("proxy"));
    }

    public static String banReason(Ban ban){
        return Utils.cancelReason("&b&lYou've been banned",
                "&7",
                "&cBan type: &6" + (ban.isPerm() ? "permanent" : "temporary"),
                "&7Nick: &e" + ban.getName(),
                "&7UUID: &e" + (ban.getUuid() == null ? "?" : ban.getUuid().toString()),
                "&7Reason: &f" + ban.getReason(),
                "&7Expired: &e" + (ban.isPerm() ? "&cnever" : TimeUtil.longToStringTime(ban.getTime())),
                "&7Banned by: &c" + ban.getAdmin(),
                "&7Received date: &e" + TimeUtil.getDate(ban.getCreated()),
                "",
                "&4You can buy unban on our site.",
                "&cDo you disagree with the ban? Refer to our discord!"
        );
    }

    public static boolean kickBan(ProxiedPlayer proxiedPlayer, Ban ban){
        if(proxiedPlayer == null) return false;
        proxiedPlayer.disconnect(banReason(ban));
        return true;
    }

}
