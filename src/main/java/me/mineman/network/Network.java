package me.mineman.network;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import me.mineman.network.command.*;
import me.mineman.network.constant.MojangAPIHelper;
import me.mineman.network.database.Database;
import me.mineman.network.database.FrequentQueryExecutor;
import me.mineman.network.listener.*;
import me.mineman.network.nick.ProxyNickSystem;
import me.mineman.network.permission.PermissionSystem;
import me.mineman.network.rank.ProxyRankSystem;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "network",
        name = "Network",
        version = BuildConstants.VERSION,
        authors = {"GhastCraftHD"}
)
public class Network {

    public static final MinecraftChannelIdentifier CHANNEL_IDENTIFIER = MinecraftChannelIdentifier.from("mineman:main");

    private final Logger logger;
    private final ProxyServer proxy;
    private final Path dataPath;

    private Database database;
    private ProxyRankSystem rankSystem;
    private PermissionSystem permissionSystem;
    //private ProxyNickSystem nickSystem;

    @Inject
    public Network(ProxyServer proxy, Logger logger, @DataDirectory Path dataPath){
        this.proxy = proxy;
        this.logger = logger;
        this.dataPath = dataPath;
        initialiseDatabase();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        registerListeners();
        registerCommands();
        proxy.getChannelRegistrar().register(CHANNEL_IDENTIFIER);
        this.rankSystem = new ProxyRankSystem(this);
        this.permissionSystem = new PermissionSystem(this);
        //this.nickSystem = new ProxyNickSystem(this);
        MojangAPIHelper.logger = this.logger;
    }

    private void initialiseDatabase(){
        this.database = new Database(this);
    }

    private void registerListeners(){
        proxy.getEventManager().register(this, new PreLoginListener(this));
        proxy.getEventManager().register(this, new ServerPostConnectListener(this));
        proxy.getEventManager().register(this, new PluginMessageListener(this));
        proxy.getEventManager().register(this, new DisconnectListener(this));
        proxy.getEventManager().register(this, new PermissionsSetupListener(this));
    }

    private void registerCommands(){
        CommandManager commandManager = proxy.getCommandManager();

        CommandMeta rankMeta = commandManager.metaBuilder("rank")
                .plugin(this)
                .build();
        SimpleCommand rankCommand = new RankCommand(this);
        commandManager.register(rankMeta, rankCommand);

        CommandMeta hubMeta = commandManager.metaBuilder("hub")
                .aliases("l", "leave")
                .plugin(this)
                .build();

        RawCommand hubCommand = new HubCommand(this);
        commandManager.register(hubMeta, hubCommand);

        CommandMeta banMeta = commandManager.metaBuilder("ban")
                .plugin(this)
                .build();

        SimpleCommand banCommand = new BanCommand(this);
        commandManager.register(banMeta, banCommand);

        CommandMeta pardonMeta = commandManager.metaBuilder("pardon")
                .plugin(this)
                .build();

        SimpleCommand pardonCommand = new PardonCommand(this);
        commandManager.register(pardonMeta, pardonCommand);

        CommandMeta pingMeta = commandManager.metaBuilder("ping")
                .plugin(this)
                .build();

        RawCommand pingCommand = new PingCommand();
        commandManager.register(pingMeta, pingCommand);

        CommandMeta joinMeta = commandManager.metaBuilder("join")
                .plugin(this)
                .build();

        SimpleCommand joinCommand = new JoinCommand(this);
        commandManager.register(joinMeta, joinCommand);

        CommandMeta endMeta = commandManager.metaBuilder("end")
                .plugin(this)
                .build();

        RawCommand endCommand = new EndCommand(this);
        commandManager.register(endMeta, endCommand);

    }

    public Logger getLogger() {
        return logger;
    }

    public ProxyServer getProxy() {
        return proxy;
    }

    public Path getDataPath() {
        return dataPath;
    }

    public Database getDatabase(){
        return database;
    }

    public FrequentQueryExecutor getFrequentQueries(){
        return database.getFrequentQueryExecutor();
    }

    public ProxyRankSystem getRankSystem(){
        return rankSystem;
    }

    public PermissionSystem getPermissionSystem() {
        return permissionSystem;
    }

    /*public ProxyNickSystem getNickSystem() {
        return nickSystem;
    }*/
}
