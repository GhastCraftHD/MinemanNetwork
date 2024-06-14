package me.mineman.network.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.mineman.network.Network;
import me.mineman.network.constant.Message;
import me.mineman.network.constant.MojangAPIHelper;

import java.util.*;
import java.util.stream.Collectors;

public class BanCommand implements SimpleCommand {

    private final Network main;

    public BanCommand(Network main) {
        this.main = main;
    }

    @Override
    public void execute(Invocation invocation) {
        main.getProxy().getScheduler().buildTask(
            main,
            () -> {
                CommandSource source = invocation.source();
                String[] args = invocation.arguments();

                if(!(source instanceof Player player)) return;
                if(args.length < 1) {
                    player.sendMessage(Message.specifyPlayer());
                    return;
                }

                Optional<UUID> optionalUUID = MojangAPIHelper.getUUIDFromUsername(args[0]);
                if(optionalUUID.isEmpty()){
                    player.sendMessage(Message.couldNotFindPlayerFromUsername());
                    return;
                }

                UUID targetUUID = optionalUUID.orElseThrow();

                if (args.length == 1) {
                    banPlayer(player, targetUUID, args[0], "No reason specified");
                } else {
                    banPlayer(player, targetUUID, args[0], String.join(" ", Arrays.asList(args).subList(1, args.length)));
                }
            }
        ).schedule();

    }

    private void banPlayer(Player player, UUID bannedUUID, String bannedName, String reason){
        if(!main.getFrequentQueries().isBanned(bannedUUID)) {
            main.getFrequentQueries().banPlayer(player.getUniqueId(), bannedUUID, reason);
            main.getProxy().getPlayer(bannedUUID).ifPresent(p -> p.disconnect(Message.banned(reason)));
            player.sendMessage(Message.bannedPlayer(bannedName, reason));
        }else{
            player.sendMessage(Message.playerAlreadyBanned(bannedName));
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        List<String> results = new ArrayList<>();
        String[] args = invocation.arguments();
        if (args.length == 1) {
            for (Player target : main.getProxy().getAllPlayers()) {
                results.add(target.getUsername());
            }
            return results.stream().
                    filter(val -> val.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return results;
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        if(!(invocation.source() instanceof Player player)) return false;
        return main.getPermissionSystem().hasPermission(player.getUniqueId(), "mineman.admin");
    }
}
