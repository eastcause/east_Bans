package me.eastcause.bans.manager.ban;

import me.eastcause.bans.model.Ban;

import java.util.UUID;

public interface BanRepositoryIO {

    boolean banPlayer(UUID uuid, String name, String reason, long time, boolean perm, String admin, boolean ignoreBan);

    boolean putBan(Ban ban);

    boolean unban(Ban ban);

    Ban getBan(String name);

    Ban getBan(UUID uuid);

    boolean hasBan(String name);

    boolean banExpired(String name);

}
