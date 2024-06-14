package me.mineman.network.nick;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import me.mineman.network.Network;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Deprecated
public class ProxyNickSystem {

    private final Network main;
    private final HashMap<UUID, String> nickCache;

    public ProxyNickSystem(Network main) {
        this.main = main;
        this.nickCache = new HashMap<>();
    }

    public void sendNickInformation(Player player){
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        String nick = getNick(player.getUniqueId());
        output.writeUTF("NickInformation");
        output.writeUTF(nick);

        Optional<ServerConnection> connection = player.getCurrentServer();
        connection.ifPresent(
                serverConnection -> serverConnection.sendPluginMessage(Network.CHANNEL_IDENTIFIER, output.toByteArray()));

    }

    public void add(UUID uuid, String nick){
        nickCache.put(uuid, nick);
    }

    public void remove(UUID uuid){
        nickCache.remove(uuid);
    }

    public void change(UUID uuid, String nick){
        remove(uuid);
        add(uuid, nick);
    }

    public String getNick(UUID uuid){
        if(nickCache.containsKey(uuid)) return nickCache.get(uuid);
        add(uuid, main.getFrequentQueries().getNicknameFromDatabase(uuid));
        return nickCache.get(uuid);
    }

    public boolean hasNick(UUID uuid){
        return getNick(uuid) != "";
    }

}
