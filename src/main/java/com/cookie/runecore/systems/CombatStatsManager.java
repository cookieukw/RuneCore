package com.cookie.runecore.systems;

import com.cookie.runecore.api.CombatStats;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Manages combat stats ONLY for connected players.
 * Stats live in memory — not persisted, not applied to arbitrary entities.
 */
public class CombatStatsManager {

    private static final Logger LOG = Logger.getLogger("RuneCore");

    private static CombatStatsManager instance;
    private final Map<UUID, CombatStats> playerStats = new ConcurrentHashMap<>();

    public CombatStatsManager(EventRegistry eventRegistry) {
        instance = this;
        eventRegistry.registerGlobal(PlayerReadyEvent.class, this::onPlayerReady);
        eventRegistry.registerGlobal(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
    }

    public static CombatStatsManager get() {
        return instance;
    }

    public CombatStats getStats(UUID playerId) {
        return playerStats.get(playerId);
    }

    public CombatStats getOrCreate(UUID playerId) {
        return playerStats.computeIfAbsent(playerId, k -> new CombatStats());
    }

    public boolean hasStats(UUID playerId) {
        return playerStats.containsKey(playerId);
    }

    private void onPlayerReady(PlayerReadyEvent event) {
        UUID uuid = event.getPlayer().getPlayerRef().getUuid();
        if (uuid != null) {
            playerStats.computeIfAbsent(uuid, k -> new CombatStats());
            LOG.fine("[RuneCore-Combat] Initialized combat stats for " + uuid);
        }
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        UUID uuid = event.getPlayerRef().getUuid();
        if (uuid != null) {
            playerStats.remove(uuid);
            LOG.fine("[RuneCore-Combat] Cleaned up combat stats for " + uuid);
        }
    }
}
