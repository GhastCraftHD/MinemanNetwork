package me.mineman.network.player;

import java.util.UUID;

public record DatabasePlayerWithId(String id, UUID uuid, String nickname, String[] permissions) {}
