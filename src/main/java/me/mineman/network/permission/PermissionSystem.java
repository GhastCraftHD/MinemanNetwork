package me.mineman.network.permission;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import me.mineman.network.Network;
import me.mineman.network.rank.Rank;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PermissionSystem {

    private final Network main;
    private final PermissionLoader loader;
    private final PermissionManager manager;

    public PermissionSystem(Network main) {
        this.main = main;
        this.loader = new PermissionLoader(main);
        this.manager = new PermissionManager();
    }

    @Deprecated
    public void sendPermissionInformation(Player player){
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        Rank rank = main.getRankSystem().getRank(player.getUniqueId());

        output.writeUTF("PermissionInformation");
        output.writeUTF(getPermissions(rank).toString());

        Optional<ServerConnection> connection = player.getCurrentServer();
        connection.ifPresent(
                serverConnection -> serverConnection.sendPluginMessage(Network.CHANNEL_IDENTIFIER, output.toByteArray()));
        player.sendMessage(Component.text("UPDATED PERMISSIONS"));
    }

    public List<String> getPermissions(Rank rank){
        List<String> permissions;

        switch (rank){
            case ADMINISTRATOR -> permissions = loader.getAdminPerms();
            case MEMBER -> permissions = loader.getMemberPerms();
            case PLAYER -> permissions = loader.getPlayerPerms();
            case GUEST -> permissions = loader.getGuestPerms();
            default -> throw new IllegalStateException("Unexpected value: " + rank);
        }

        return permissions;
    }

    public PermissionManager getManager() {
        return manager;
    }

    public boolean hasPermission(UUID uuid, String permission){
        return manager.hasPermission(uuid, permission);
    }
}
