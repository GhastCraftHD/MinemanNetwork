package me.mineman.network.command;

import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.mineman.network.Network;

public class HubCommand implements RawCommand {

    private Network main;

    public HubCommand(Network main) {
        this.main = main;
    }

    @Override
    public void execute(Invocation invocation) {
        if(!(invocation.source() instanceof Player player)) return;

        player.createConnectionRequest(main.getProxy().getServer("lobby").orElseThrow()).connect();
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return true;
    }
}
