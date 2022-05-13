package me.eastcause.bans.model;

import lombok.Data;
import me.eastcause.bans.manager.ban.BanManager;

import java.util.UUID;
@Data
public class Ban {

    private UUID uuid;
    private String name;
    private String reason;
    private long time;
    private boolean perm;
    private String server;
    private String admin;
    private long created;

    /**
     *
     * @param uuid
     * @param name
     * @param reason
     * @param time
     * @param perm
     * @param server
     * @param admin
     */

    public Ban(UUID uuid, String name, String reason, long time, boolean perm, String server, String admin){
        this.uuid = uuid;
        this.name = name;
        this.reason = reason;
        this.time = time;
        this.perm = perm;
        this.server = server;
        this.admin = admin;
        this.created = System.currentTimeMillis();
    }

    public boolean expired(){
        if(isPerm()) return false;
        if(getTime() > System.currentTimeMillis()) return false;
        BanManager.unbanPlayer(name, server);
        return true;
    }
}
