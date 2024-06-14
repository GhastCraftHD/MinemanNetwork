package me.mineman.network.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.mineman.network.Network;
import me.mineman.network.constant.Message;
import me.mineman.network.constant.MojangAPIHelper;
import me.mineman.network.constant.Permission;
import me.mineman.network.rank.Rank;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class RankCommand implements SimpleCommand {

    private final Network main;

    public RankCommand(Network main) {
        this.main = main;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if(!(source instanceof Player player)) return;

        switch(args.length){
            case 0 -> {
                Rank rank = main.getRankSystem().getRank(player.getUniqueId());
                source.sendMessage(Message.rankGetResponse(player, rank));
            }
            case 3 -> {
                if (!args[0].equalsIgnoreCase("set")) return;
                
                Optional<Player> optionalTarget = main.getProxy().getPlayer(args[1]);

                UUID uuid;

                if(optionalTarget.isPresent()){
                    uuid = optionalTarget.orElseThrow().getUniqueId();
                }else{
                    Optional<UUID> optionalUUID = MojangAPIHelper.getUUIDFromUsername(args[1]);
                    if(optionalUUID.isPresent()){
                        uuid = optionalUUID.orElseThrow();
                    }else{
                        source.sendMessage(Message.couldNotFindPlayer(args[1]));
                        return;
                    }
                }

                if(!main.getFrequentQueries().hasDatabaseEntry(uuid)) {
                    main.getFrequentQueries().createDatabaseEntry(uuid);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignore){}
                }

                Rank rank;
                try {
                    rank = Rank.valueOf(args[2]);
                } catch (IllegalArgumentException e) {
                    source.sendMessage(Message.illegalRank());
                    return;
                }

                main.getRankSystem().setRank(uuid, rank);

                if(player.getUniqueId() == uuid) player.sendMessage(Message.changedRankSelf(rank));
                else{
                    optionalTarget.ifPresentOrElse(
                            target -> {
                                target.sendMessage(Message.changedRankTarget(
                                        player.getUsername(),
                                        main.getRankSystem().getRank(player.getUniqueId()),
                                        rank));

                                player.sendMessage(Message.changedRankSender(target.getUsername(), rank));
                            },
                            () -> player.sendMessage(Message.changedRankSender(args[1], rank))
                    );


                }
            }
        }

    }

    @Override
    public List<String> suggest(Invocation invocation) {
        List<String> results = new ArrayList<>();
        String[] args = invocation.arguments();
        switch(args.length){
            case 1 -> {
                results.add("set");
                return results.stream().
                        filter(val -> val.toLowerCase().startsWith(args[0].toLowerCase()))
                        .toList();
            }
            case 2 -> {
                for (Player target : main.getProxy().getAllPlayers()) {
                    results.add(target.getUsername());
                }
                return results.stream().
                        filter(val -> val.toLowerCase().startsWith(args[1].toLowerCase()))
                        .toList();
            }
            case 3 -> {
                for (Rank rank : Rank.values()) {
                    results.add(rank.name());
                }
                return results.stream().
                        filter(val -> val.toLowerCase().startsWith(args[2].toLowerCase()))
                        .toList();
            }
        }
        return results;
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        if(!(invocation.source() instanceof Player player)) return false;
        return main.getPermissionSystem().hasPermission(player.getUniqueId(), Permission.ADMIN);
    }

}
