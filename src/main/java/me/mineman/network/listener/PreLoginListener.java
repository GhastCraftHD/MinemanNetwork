package me.mineman.network.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.proxy.InboundConnection;
import me.mineman.network.Network;
import me.mineman.network.constant.Message;
import me.mineman.network.rank.Rank;

import java.net.InetSocketAddress;
import java.util.Optional;

public class PreLoginListener {

    private final Network main;

    public PreLoginListener(Network main) {
        this.main = main;
    }

    @Subscribe
    public void onPreLogin(PreLoginEvent e){
        if(e.getUniqueId() == null) return;

        InboundConnection connection = e.getConnection();
        Optional<String> forcedHost = connection.getVirtualHost().map(InetSocketAddress::getHostName);

        if(main.getFrequentQueries().isBanned(e.getUniqueId())){
            String reason = main.getFrequentQueries().getBanReason(e.getUniqueId());
            e.setResult(PreLoginEvent.PreLoginComponentResult.denied(Message.banned(reason)));
        }

        if(!main.getFrequentQueries().hasDatabaseEntry(e.getUniqueId())){
            main.getFrequentQueries().createDatabaseEntry(e.getUniqueId());
        }

        Rank rank = main.getRankSystem().getRank(e.getUniqueId());

        forcedHost.ifPresent(host -> {
            if(!host.equals("survival.mineman.me")) return;
            if(rank.ordinal() == Rank.ADMINISTRATOR.ordinal()) return;
            e.setResult(PreLoginEvent.PreLoginComponentResult.denied(Message.SURVIVAL_DENY_JOIN));
            main.getRankSystem().remove(e.getUniqueId());
        });

        main.getPermissionSystem().getManager().add(
                e.getUniqueId(),
                main.getPermissionSystem().getPermissions(
                        rank));
    }


}
