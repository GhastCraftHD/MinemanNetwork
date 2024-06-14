package me.mineman.network.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.mineman.network.Network;
import me.mineman.network.constant.Message;

import java.util.Optional;

public class PluginMessageListener {

    private final Network main;

    public PluginMessageListener(Network main) {
        this.main = main;
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent e){
        if(!(e.getSource() instanceof ServerConnection connection)) return;
        if(e.getIdentifier() != Network.CHANNEL_IDENTIFIER) return;

        ByteArrayDataInput input = ByteStreams.newDataInput(e.getData());

        String subchannel = input.readUTF();

        if(subchannel.equals("ConnectionRequest")){
            String servername = input.readUTF();
            Optional<RegisteredServer> optionalConnection = main.getProxy().getServer(servername);
            optionalConnection.ifPresentOrElse(
                    conn -> connection.getPlayer().createConnectionRequest(conn).connectWithIndication(),
                    () -> connection.getPlayer().sendMessage(Message.unableToConnect(servername))
            );
        }


    }

}
