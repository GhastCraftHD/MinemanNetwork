package me.mineman.network.database;

import com.velocitypowered.api.plugin.PluginContainer;
import me.mineman.network.Network;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

public class CredentialManager {

    private final Network main;
    private final YamlConfigurationLoader loader;
    private CommentedConfigurationNode root;
    private String user;
    private String pass;
    private String namespace;
    private String database;

    public CredentialManager(Network main){
        this.main = main;
        loader = YamlConfigurationLoader.builder()
                .path(Paths.get(main.getDataPath().toString(), "database.yml"))
                .build();

        loadCredentialFile();
        initialiseValues();
    }

    private void loadCredentialFile(){
        try {
            root = loader.load();
        } catch (IOException e) {
            main.getLogger().error("Could not load database credentials!");
            Optional<PluginContainer> container = main.getProxy().getPluginManager().getPlugin("network");
            container.ifPresent(pluginContainer -> pluginContainer.getExecutorService().shutdown());
        }
    }

    private void initialiseValues(){
        user = root.node("user").getString();
        pass = root.node("pass").getString();
        namespace = root.node("namespace").getString();
        database = root.node("database").getString();
    }

    public DatabaseCredentials getDatabaseCredentials(){
        return new DatabaseCredentials(user, pass, namespace, database);
    }
}
