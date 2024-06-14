package me.mineman.network.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.mineman.network.Network;
import me.mineman.network.constant.Message;
import me.mineman.network.constant.Permission;
import me.mineman.network.rank.Rank;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JoinCommand implements SimpleCommand {

    private final Network main;

    public JoinCommand(Network main) {
        this.main = main;
    }

    @Override
    public void execute(Invocation invocation) {
        if(!(invocation.source() instanceof Player player)) return;
        String[] args = invocation.arguments();
        if(!(args.length >= 1)) {
            player.sendMessage(Message.noServerProvided());
            return;
        }
        Optional<RegisteredServer> optionalServer = main.getProxy().getServer(args[0]);
        if(optionalServer.isEmpty()){
            player.sendMessage(Message.unableToConnect(args[0]));
            return;
        }

        player.createConnectionRequest(optionalServer.orElseThrow()).connect();

    }

    @Override
    public List<String> suggest(final Invocation invocation){
        if(invocation.arguments().length != 1) return new ArrayList<>();

        return main.getProxy().getAllServers()
                .stream()
                .map(server -> server.getServerInfo().getName())
                .filter(val -> val.toLowerCase().startsWith(invocation.arguments()[0].toLowerCase()))
                .toList();

    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        if(!(invocation.source() instanceof Player player)) return false;
        return main.getPermissionSystem().hasPermission(player.getUniqueId(), Permission.ADMIN);
    }
}
