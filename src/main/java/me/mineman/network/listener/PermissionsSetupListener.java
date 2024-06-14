package me.mineman.network.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.permission.PermissionsSetupEvent;
import com.velocitypowered.api.permission.PermissionFunction;
import com.velocitypowered.api.proxy.Player;
import me.mineman.network.Network;
import me.mineman.network.rank.Rank;

public class PermissionsSetupListener {

    private final Network main;

    public PermissionsSetupListener(Network main) {
        this.main = main;
    }

    @Subscribe
    public void onPermissionsSetup(PermissionsSetupEvent e){
        if(!(e.getSubject() instanceof Player player)) return;
        if(main.getRankSystem().hasRank(player.getUniqueId(), Rank.ADMINISTRATOR)){
            e.setProvider(subject -> PermissionFunction.ALWAYS_TRUE);
        }else{
            e.setProvider(subject -> PermissionFunction.ALWAYS_FALSE);
        }
    }

}
