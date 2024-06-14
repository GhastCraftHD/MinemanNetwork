package me.mineman.network.command;

import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import me.mineman.network.constant.Message;

public class PingCommand implements RawCommand {
    @Override
    public void execute(Invocation invocation) {
        if(!(invocation.source() instanceof Player player)) return;
        player.sendMessage(Message.pingResponse(player.getPing()));
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return true;
    }
}
