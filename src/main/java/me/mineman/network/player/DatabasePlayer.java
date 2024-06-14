package me.mineman.network.player;

import java.util.UUID;

public record DatabasePlayer(UUID uuid, String nickname, String[] permissions) {}
