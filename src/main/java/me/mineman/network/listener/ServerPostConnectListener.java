package me.mineman.network.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.Player;
import me.mineman.network.Network;

public class ServerPostConnectListener {

    private final Network main;

    public ServerPostConnectListener(Network main) {
        this.main = main;
    }

    @Subscribe
    public void onServerPostConnect(ServerPostConnectEvent e){
        Player player = e.getPlayer();
        //main.getNickSystem().sendNickInformation(player);
        main.getRankSystem().sendRankInformation(player);
        //main.getPermissionSystem().sendPermissionInformation(player);
    }

}
