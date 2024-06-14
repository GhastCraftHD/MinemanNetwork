package me.mineman.network.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.mineman.network.Network;
import me.mineman.network.constant.Message;
import me.mineman.network.constant.MojangAPIHelper;
import me.mineman.network.constant.Permission;

import java.util.Optional;
import java.util.UUID;

public class PardonCommand implements SimpleCommand {

    private Network main;

    public PardonCommand(Network main) {
        this.main = main;
    }

    @Override
    public void execute(Invocation invocation) {
        main.getProxy().getScheduler().buildTask(
                main,
                () -> {
                    CommandSource source = invocation.source();
                    String[] args = invocation.arguments();

                    if(!(source instanceof Player player)) return;
                    if(args.length < 1) {
                        player.sendMessage(Message.specifyPlayer());
                        return;
                    }

                    Optional<UUID> optionalUUID = MojangAPIHelper.getUUIDFromUsername(args[0]);
                    if(optionalUUID.isEmpty()){
                        player.sendMessage(Message.couldNotFindPlayerFromUsername());
                        return;
                    }

                    UUID uuid = optionalUUID.orElseThrow();

                    if(main.getFrequentQueries().isBanned(uuid)){
                        main.getFrequentQueries().pardonPlayer(uuid);

                        player.sendMessage(Message.pardonedPlayer(args[0]));
                    }else{
                        player.sendMessage(Message.playerIsNotBanned(args[0]));
                    }


                }
        ).schedule();

    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        if(!(invocation.source() instanceof Player player)) return false;
        return main.getPermissionSystem().hasPermission(player.getUniqueId(), Permission.ADMIN);
    }
}
