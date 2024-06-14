package me.mineman.network.permission;

import com.velocitypowered.api.plugin.PluginContainer;
import me.mineman.network.Network;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class PermissionLoader {

    private final Network main;
    private final YamlConfigurationLoader loader;
    private ConfigurationNode root;
    private List<String> adminPerms;
    private List<String> memberPerms;
    private List<String> playerPerms;
    private List<String> guestPerms;

    public PermissionLoader(Network main){
        this.main = main;
        loader = YamlConfigurationLoader.builder()
                .path(Paths.get(main.getDataPath().toString(), "permissions.yml"))
                .build();

        loadPermissionFile();
        try {
            initialiseValues();
        } catch (SerializationException e) {
            main.getLogger().error("Could not load permissions");
            Optional<PluginContainer> container = main.getProxy().getPluginManager().getPlugin("network");
            container.ifPresent(pluginContainer -> pluginContainer.getExecutorService().shutdown());
        }
    }

    private void loadPermissionFile(){
        try {
            root = loader.load();
        } catch (IOException e) {
            main.getLogger().error("Could not load permission file");
            Optional<PluginContainer> container = main.getProxy().getPluginManager().getPlugin("network");
            container.ifPresent(pluginContainer -> pluginContainer.getExecutorService().shutdown());
        }
    }

    private void initialiseValues() throws SerializationException {
        guestPerms = root.node("guest").getList(String.class);
        playerPerms = root.node("player").getList(String.class);
        playerPerms.addAll(guestPerms);
        memberPerms = root.node("member").getList(String.class);
        memberPerms.addAll(playerPerms);
        adminPerms = root.node("admin").getList(String.class);
        adminPerms.addAll(memberPerms);
    }

    public List<String> getAdminPerms() {
        return adminPerms;
    }

    public List<String> getMemberPerms() {
        return memberPerms;
    }

    public List<String> getPlayerPerms() {
        return playerPerms;
    }

    public List<String> getGuestPerms() {
        return guestPerms;
    }
}
