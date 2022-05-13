package me.eastcause.bans.model;

import lombok.Data;

@Data
public class Result {

    private String nick;
    private String result;
    private boolean banned;

    public Result(String nick, String result, boolean banned){
        this.nick = nick;
        this.result = result;
        this.banned = banned;
    }
}
