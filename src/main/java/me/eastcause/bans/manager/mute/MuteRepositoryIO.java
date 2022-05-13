package me.eastcause.bans.manager.mute;

import me.eastcause.bans.model.Mute;

import java.util.UUID;

public interface MuteRepositoryIO {

    boolean mutePlayer(UUID uuid, String name, String reason, long time, boolean perm, String admin, boolean ignoreBan);

    boolean putMute(Mute mute);

    boolean unMute(Mute mute);

    Mute getMute(String name);

    Mute getMute(UUID uuid);

    boolean hasMute(String name);

    boolean muteExpired(String name);

}
