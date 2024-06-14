package me.mineman.network.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import me.mineman.network.Network;

public class DisconnectListener {

    private final Network main;

    public DisconnectListener(Network main) {
        this.main = main;
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent e){
        main.getPermissionSystem().getManager().remove(e.getPlayer().getUniqueId());
        main.getRankSystem().remove(e.getPlayer().getUniqueId());
    }

}
