package me.mineman.network.command;

import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import me.mineman.network.Network;
import me.mineman.network.constant.Message;
import me.mineman.network.constant.Permission;
import me.mineman.network.rank.Rank;

public class EndCommand implements RawCommand {

    private final Network main;

    public EndCommand(Network main) {
        this.main = main;
    }

    @Override
    public void execute(Invocation invocation) {
        main.getProxy().shutdown(Message.RESTARTING);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        if(!(invocation.source() instanceof Player player)) return false;
        return main.getPermissionSystem().hasPermission(player.getUniqueId(), Permission.ADMIN);
    }
}
