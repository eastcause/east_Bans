package me.eastcause.bans.command.impl;

import lombok.Getter;
import me.eastcause.bans.command.Performer;
import me.eastcause.bans.command.Cmd;
import me.eastcause.bans.manager.ban.BanManager;
import me.eastcause.bans.manager.ban.BanRepository;
import me.eastcause.bans.utils.Utils;
import net.md_5.bungee.api.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUnbanCommand extends Cmd {
    public GUnbanCommand() {
        super("gunban");
        argument = (sender, args) -> {
            if(args.length == 0){
                return Utils.sendMessage(sender, "&cUsage: &6/gunban <nick> <reason> <server ? server=serverName>");
            }
            String nick = args[0].toLowerCase();
            String reason = "No Reason";
            String admin = sender.getName();
            List<String> servers = new ArrayList<>();
            if(args.length == 1){
                boolean unban = false;
                StringBuilder s = new StringBuilder();
                for(BanRepository banRepository : BanManager.getREPOSITORIES().values()){
                    if(banRepository.hasBan(nick)){
                        banRepository.unban(banRepository.getBan(nick));
                        unban = true;
                        s.append(banRepository.getServer()).append(", ");
                    }
                }
                if(!unban){
                    return Utils.sendMessage(sender, "&CThis player is not banned!");
                }
                s = new StringBuilder(s.substring(0, s.length() - 2));
                return Utils.sendMessage("&bPlayer &6"+nick+" &bwas unbanned by &6"+admin+" &bon the server &6"+s.toString()+" &bwith the reason: &7No Reason!", getPermission());
            }
            boolean proxy = true;
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
                if (BanManager.hasBan(nick, "proxy")) {
                    BanManager.unbanPlayer(nick, "proxy");
                    Utils.sendMessage("&bPlayer &6" + nick + " &bwas unbanned by &6" + admin + " &bon the server &6proxy &bwith the reason: &7"+reason+"!", getPermission());
                } else {
                    return Utils.sendMessage(sender, "&cThis player is not banned from the proxy!");
                }
            }else{
                boolean unban = false;
                StringBuilder s = new StringBuilder();
                for(String server : servers){
                    if (BanManager.hasBan(nick, server)) {
                        BanManager.unbanPlayer(nick, server);
                        unban = true;
                        s.append(sender).append(", ");
                    }
                }
                if(!unban){
                    return Utils.sendMessage(sender, "&cThis player is not banned from these servers!");
                }
                s = new StringBuilder(s.substring(0, s.length() - 2));
                Utils.sendMessage("&bPlayer &6" + nick + " &bwas unbanned by &6" + admin + " &bon the server &6"+s+" &bwith the reason: &7"+reason+"!", getPermission());
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
