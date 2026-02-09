package com.cookie.runecore.systems.ui;

import com.cookie.runecore.api.PlayerStats;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.event.EventRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class ManaHudManager {

    private final Map<UUID, ManaHud> activeHuds = new HashMap<>();
    private final Timer updateTimer = new Timer(true);

    public ManaHudManager(EventRegistry eventRegistry) {
        eventRegistry.register(PlayerConnectEvent.class, this::onPlayerConnect);
        eventRegistry.register(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
        
        // Start periodic update to sync mana values to client HUDs
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateAllHuds();
            }
        }, 1000, 500); // Initial delay 1s, repeat every 500ms
    }

    private void onPlayerConnect(PlayerConnectEvent event) {
        PlayerRef playerRef = event.getPlayerRef();
        ManaHud hud = new ManaHud(playerRef);
        synchronized (activeHuds) {
            activeHuds.put(playerRef.getUuid(), hud);
        }
        
        // Initial show
        hud.show();
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        synchronized (activeHuds) {
            activeHuds.remove(event.getPlayerRef().getUuid());
        }
    }

    private void updateAllHuds() {
        synchronized (activeHuds) {
            for (ManaHud hud : activeHuds.values()) {
                PlayerStats stats = new PlayerStats(hud.getPlayerRef());
                stats.getMana().thenAccept(current -> {
                    // Assuming max mana is 100 for now. Base Hytale mana is usually 100.
                    hud.updateMana(current, 100.0f);
                }).exceptionally(e -> {
                    System.err.println("Error updating HUD for player: " + e.getMessage());
                    return null;
                });
            }
        }
    }
}
