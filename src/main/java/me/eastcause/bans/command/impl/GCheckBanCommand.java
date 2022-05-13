package me.eastcause.bans.command.impl;

import lombok.Getter;
import me.eastcause.bans.command.Performer;
import me.eastcause.bans.command.Cmd;
import me.eastcause.bans.manager.ban.BanManager;
import me.eastcause.bans.manager.ban.BanRepository;
import me.eastcause.bans.model.Ban;
import me.eastcause.bans.utils.Utils;
import me.eastcause.bans.utils.time.TimeUtil;
import net.md_5.bungee.api.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class GCheckBanCommand extends Cmd {
    public GCheckBanCommand() {
        super("gcheckban");
        argument = (sender, args) -> {
            if(args.length == 0){
                return Utils.sendMessage(sender, "&cUsage: &6/gcheckban <nick> <server ? server=serverName>");
            }
            String name = args[0];
            if(args.length == 1){
                boolean banned = false;
                Utils.sendMessage(sender, "&bPlayer bans: &6" + name);
                for(BanRepository banRepository : BanManager.getREPOSITORIES().values()){
                    if(banRepository.hasBan(name)){
                        Ban ban = banRepository.getBan(name);
                        banned = true;
                        Utils.sendMessage(sender, "&8- &7Server &6"+banRepository.getServer()+" &7expired: &c"+(ban.isPerm() ? "never " : "&6" + TimeUtil.longToStringTime(ban.getTime()))+"&7reason: &6" + ban.getReason());
                    }
                }
                if(!banned){
                    Utils.sendMessage(sender, "&cThe player is not banned from any server!");
                }
                return false;
            }
            List<String> servers = new ArrayList<>();
            for(String arg : args){
                if(arg.startsWith("server=") || arg.startsWith("s=")){
                    if(arg.split("=").length <= 1){
                        return Utils.sendMessage(sender, "&cPlease enter a server name!");
                    }
                    String s = arg.split("=")[1];
                    String[] servs = s.split(",");
                    for(String server : servs){
                        if(server == null) continue;
                        servers.add(server);
                    }
                    break;
                }
            }
            if(servers.size() == 0){
                return Utils.sendMessage(sender, "&cPlease enter a server name!");
            }
            Utils.sendMessage(sender, "&bPlayer bans: &6" + name);
            for(String server : servers){
                BanRepository banRepository = BanManager.getRepositoryByServer(server);
                if(banRepository == null) {
                    Utils.sendMessage(sender, "&cBanRepository from server &b" + server + " &cdoes not exist!");
                    continue;
                }
                if (banRepository.hasBan(name)) {
                    Ban ban = banRepository.getBan(name);
                    Utils.sendMessage(sender, "&8- &7Server &6" + banRepository.getServer() + " &7expired: &c" + (ban.isPerm() ? "never " : "&6" + TimeUtil.longToStringTime(ban.getTime())) + "&7reason: &6" + ban.getReason());
                }else{
                    Utils.sendMessage(sender, "&8- &cThe player does not have a ban on the server: &b" + banRepository.getServer());
                }
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
