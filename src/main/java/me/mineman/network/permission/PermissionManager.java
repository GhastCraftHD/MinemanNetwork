package me.mineman.network.permission;

import me.mineman.network.Network;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PermissionManager {

    private final Map<UUID, List<String>> playerPermissions;

    public PermissionManager() {
        this.playerPermissions = new HashMap<>();
    }

    public void add(UUID uuid, List<String> permissions ){
        playerPermissions.put(uuid, permissions);
    }

    public void remove(UUID uuid){
        playerPermissions.remove(uuid);
    }

    public boolean hasPermission(UUID uuid, String permission){
        return playerPermissions.get(uuid).contains(permission);
    }



}
