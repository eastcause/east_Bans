package me.eastcause.bans.command.impl;

import lombok.Getter;
import me.eastcause.bans.BanPlugin;
import me.eastcause.bans.command.Cmd;
import me.eastcause.bans.command.Performer;
import me.eastcause.bans.manager.ban.BanManager;
import me.eastcause.bans.manager.mute.MuteManager;
import me.eastcause.bans.utils.Utils;
import me.eastcause.bans.utils.time.TimeUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GMuteCommand extends Cmd {
    public GMuteCommand() {
        super("gmute");
        argument = (sender, args) -> {
            if(args.length == 0){
                return Utils.sendMessage(sender, "&cUsage: &6/gmute <nick> <time> <reason> <server ? server=serverName>");
            }
            String name = args[0];
            UUID uuid = null;
            ProxiedPlayer player = BanPlugin.getBanPlugin().getProxy().getPlayer(name);
            if(player != null){
                name = player.getName();
                uuid = player.getUniqueId();
            }
            String admin = sender.getName();
            String reason = "No Reason";
            long time = 0L;
            boolean perm = true;
            boolean proxy = true;
            List<String> servers = new ArrayList<>();
            if(args.length == 1){
                Utils.sendMessage("&bPlayer: &6"+name+" &bwas &6permanently &bmutated by &6"+admin+" &bwith reason: &7"+reason+" &bon the &6proxy &bserver.", getPermission());
                ProxiedPlayer proxiedPlayer = BanPlugin.getBanPlugin().getProxy().getPlayer(name);
                if(proxiedPlayer != null){
                    Utils.sendMessage(proxiedPlayer, "&bYou have been &6permanently &bmutated &bby &6"+admin+" &bwith reason: &7"+reason+" &bon the &6proxy &bserver.");
                }
                return MuteManager.mutePlayer(uuid, name, reason, time, true, admin, true, "proxy");
            }
            List<String> reasonWithout = new ArrayList<>();
            reasonWithout.add(args[0]);
            for(String arg : args){
                if(arg.startsWith("server=") || arg.startsWith("s=")){
                    if(arg.split("=").length <= 1){
                        return Utils.sendMessage(sender, "&cPlease enter a server name!");
                    }
                    reasonWithout.add(arg);
                    String s = arg.split("=")[1];
                    String[] servs = s.split(",");
                    boolean p = Arrays.stream(servs).anyMatch(s1 -> s1.equalsIgnoreCase("proxy"));
                    if(p){
                        break;
                    }else{
                        proxy = false;
                        for(String server : servs){
                            if(server == null) continue;
                            servers.add(server);
                        }
                    }
                    break;
                }
            }
            if(!proxy && servers.size() == 0){
                return Utils.sendMessage(sender, "&cPlease enter a server name!");
            }
            for(String arg : args) {
                if(reasonWithout.contains(arg)) continue;
                long t = TimeUtil.parseDateDiff(arg, true);
                if (t > System.currentTimeMillis()) {
                    time = t;
                    perm = false;
                    reasonWithout.add(arg);
                    break;
                }
            }
            StringBuilder r = new StringBuilder();
            for(String arg : args){
                if(!reasonWithout.contains(arg)){
                    r.append(arg).append(" ");
                }
            }
            if(r.length() > 0){
                reason = r.substring(0, r.length() - 1);
            }
            if(proxy) {
                MuteManager.mutePlayer(uuid, name, reason, time, perm, admin, true, "proxy");
            }else{
                MuteManager.mutePlayer(uuid, name, reason, time, perm, admin, true, (servers.toArray(new String[0])));
            }
            Utils.sendMessage("&bPlayer: &6"+name+" &bwas &6"+(perm ? "permanently" : "temporarily")+" &bmutated "+(perm ? "" : "for: &6" + TimeUtil.longToStringTime(time) + "")+"&bby &6"+admin+" &bwith reason: &7"+reason+" &bon the &6"+ (proxy ? "proxy" : servers.toString().toLowerCase().replace("[", "").replace("]", ""))+" &bserver.", getPermission());
            ProxiedPlayer proxiedPlayer = BanPlugin.getBanPlugin().getProxy().getPlayer(name);
            if(proxiedPlayer != null){
                Utils.sendMessage(proxiedPlayer, "&bYou have been &6"+(perm ? "permanently" : "temporarily")+" &bmutated "+(perm ? "" : "for: &6" + TimeUtil.longToStringTime(time) + "")+"&bby &6"+admin+" &bwith reason: &7"+reason+" &bon the &6"+(proxy ? "proxy" : servers.toString().toLowerCase().replace("[", "").replace("]", ""))+" &bserver.");
            }
            return false;
        };
    }

    @Getter private static Performer argument;

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
       return argument.execute(sender, args);
    }
}
