package me.eastcause.bans.manager.mute;

import lombok.Getter;
import me.eastcause.bans.configuration.Config;
import me.eastcause.bans.manager.ban.BanManager;
import me.eastcause.bans.manager.ban.BanRepository;
import me.eastcause.bans.model.Ban;
import me.eastcause.bans.model.Mute;
import me.eastcause.bans.utils.Utils;
import me.eastcause.bans.utils.time.TimeUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import rabbit.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MuteManager {

    /**
     * @param key - server name
     * @param value - BanRepository
     */
    @Getter
    private static ConcurrentHashMap<String, MuteRepository> REPOSITORIES = new ConcurrentHashMap<>();

    public static void createRepository(String server){
        getREPOSITORIES().put(server.toLowerCase(), new MuteRepository(server));
    }

    public static MuteRepository getRepositoryByServer(String server){
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

    public static boolean mutePlayer(UUID uuid, String name, String reason, long time, boolean perm, String admin, boolean ignoreBan, String... servers){
        boolean proxy = (typedProxy(servers));
        if(proxy || getREPOSITORIES().size() == 1){
            MuteRepository muteRepository = getRepositoryByServer("proxy");
            muteRepository.mutePlayer(uuid, name, reason, time, perm, admin, ignoreBan);
            return true;
        }
        if(servers.length == 1){
            MuteRepository muteRepository = getRepositoryByServer(servers[0]);
            if(muteRepository == null){
                return false;
            }
        }
        for(String server : servers){
            MuteRepository muteRepository = getRepositoryByServer(server);
            if(muteRepository == null){
                System.err.println("MuteRepository from server: "+server+" does not exist!");
                continue;
            }
            muteRepository.mutePlayer(uuid, name, reason, time, perm, admin, ignoreBan);
        }
        return true;
    }

    public static boolean unmutePlayer(String name, String... servers){
        boolean proxy = typedProxy(servers);
        if(proxy || getREPOSITORIES().size() == 1){
            MuteRepository muteRepository = getRepositoryByServer("proxy");
            if(!muteRepository.hasMute(name)){
                return false;
            }
            muteRepository.unMute(muteRepository.getMute(name));
            return true;
        }
        if(servers.length == 1){
            MuteRepository muteRepository = getRepositoryByServer(servers[0]);
            if(muteRepository == null){
                return false;
            }
        }
        for(String server : servers){
            MuteRepository muteRepository = getRepositoryByServer(server);
            if(muteRepository == null){
                System.err.println("BanRepository from server: "+server+" does not exist!");
                continue;
            }
            if(!muteRepository.hasMute(name)){
                continue;
            }
            muteRepository.unMute(muteRepository.getMute(name));
        }
        return true;
    }

    public static boolean hasMute(String name, String server){
        MuteRepository muteRepository = getRepositoryByServer(server);
        if(muteRepository == null) return false;
        return muteRepository.hasMute(name);
    }

    public static boolean hasMute(String name, MuteRepository muteRepository){
        if(muteRepository == null) return false;
        return muteRepository.hasMute(name);
    }

    public static Mute getMute(String name, String server){
        MuteRepository muteRepository = getRepositoryByServer(server);
        if(muteRepository == null) return null;
        return (hasMute(name, muteRepository) ? muteRepository.getMute(name) : null);
    }

    public static List<Mute> getMutes(String name){
        List<Mute> mutes = new ArrayList<>();
        for(MuteRepository muteRepository : getREPOSITORIES().values()){
            if(hasMute(name, muteRepository)){
                mutes.add(muteRepository.getMute(name));
            }
        }
        return mutes;
    }

    public static String getName(UUID uuid, String server){
        MuteRepository muteRepository = getRepositoryByServer(server);
        if(muteRepository == null) return null;
        Mute mute = muteRepository.getMute(uuid);
        return (mute != null ? mute.getName() : null);
    }

    private static boolean typedProxy(String... servers) {
        if (servers.length == 0) return true;
        return Arrays.stream(servers).anyMatch(s -> s.equalsIgnoreCase("proxy"));
    }

    public static boolean muteMessage(ProxiedPlayer proxiedPlayer, Mute mute){
        if(proxiedPlayer == null) return false;
        Utils.sendMessage(proxiedPlayer, "&bYou have been mutated by: &6"+mute.getAdmin()+" &bfor &6"+mute.getReason()+" &bexpires: &6"+ (mute.isPerm() ? "&cnever " : "" + TimeUtil.longToStringTime(mute.getTime())) +"&bon server: &6"+mute.getServer()+"!");
        return true;
    }
}
