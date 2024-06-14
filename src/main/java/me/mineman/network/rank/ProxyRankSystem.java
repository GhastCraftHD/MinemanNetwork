package me.mineman.network.rank;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import me.mineman.network.Network;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ProxyRankSystem {

    private final Network main;
    private final Map<UUID, Rank> rankCache;
    private final RankQueryExecutor queryExecutor;

    public ProxyRankSystem(Network main){
        this.main = main;
        this.rankCache = new HashMap<>();
        this.queryExecutor = new RankQueryExecutor(main.getDatabase());
    }

    public void sendRankInformation(Player player){
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        Rank rank = main.getRankSystem().getRank(player.getUniqueId());
        output.writeUTF("RankInformation");
        output.writeUTF(rank.name());

        Optional<ServerConnection> connection = player.getCurrentServer();
        connection.ifPresent(
                serverConnection -> serverConnection.sendPluginMessage(Network.CHANNEL_IDENTIFIER, output.toByteArray()));
    }

    public void add(UUID uuid, Rank rank){
        rankCache.put(uuid, rank);
    }

    public void remove(UUID uuid){
        rankCache.remove(uuid);
    }

    public Rank getRank(UUID uuid){
        if(rankCache.containsKey(uuid)) return rankCache.get(uuid);
        add(uuid, queryExecutor.getRankFromDatabase(uuid));
        return rankCache.get(uuid);
    }

    public void setRank(UUID uuid, Rank rank){
        remove(uuid);
        add(uuid, rank);
        main.getProxy().getScheduler()
                .buildTask(main,
                        () -> queryExecutor.setRankInDatabase(uuid, rank)
                ).schedule();
        main.getProxy().getPlayer(uuid).ifPresent(this::sendRankInformation);
    }

    public boolean hasRank(UUID uuid, Rank rank){
        return getRank(uuid) == rank;
    }



}
