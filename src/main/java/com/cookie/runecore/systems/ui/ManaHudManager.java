package com.cookie.runecore.systems.ui;

import com.cookie.runecore.api.PlayerStats;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.protocol.packets.interface_.HudComponent;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class ManaHudManager {

    private final Map<UUID, ManaHud> activeHuds = new HashMap<>();
    private final Timer updateTimer = new Timer(true);

    public ManaHudManager(EventRegistry eventRegistry) {
        eventRegistry.registerGlobal(PlayerReadyEvent.class, this::onPlayerReady);
        eventRegistry.registerGlobal(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
        
        // Start periodic update to sync mana values to client HUDs
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateAllHuds();
            }
        }, 1000, 33); // Initial delay 1s, repeat every 33ms (~30fps for animations)
    }

    private void onPlayerReady(PlayerReadyEvent event) {
        Ref<EntityStore> ref = event.getPlayerRef();
        if (ref == null || !ref.isValid()) return;
        
        Player player = event.getPlayer();
        if (player == null) return;

        PlayerRef playerRef = player.getPlayerRef();
        if (playerRef == null) return;

        System.out.println("[RuneCore-Manager] Player Ready: " + playerRef.getUsername() + ", showing mana HUD");
        
        // Hide original Hytale Mana HUD
        player.getHudManager().hideHudComponents(playerRef, HudComponent.Mana);
        
        ManaHud hud = new ManaHud(playerRef);
        synchronized (activeHuds) {
            activeHuds.put(playerRef.getUuid(), hud);
        }
        
        // Final check to prevent crash if player disconnected during event
        if (playerRef.isValid()) {
            hud.show();
        }
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        synchronized (activeHuds) {
            activeHuds.remove(event.getPlayerRef().getUuid());
        }
    }

    private void updateAllHuds() {
        synchronized (activeHuds) {
            if (activeHuds.isEmpty()) return;
            
            for (ManaHud hud : activeHuds.values()) {
                PlayerRef playerRef = hud.getPlayerRef();
                if (playerRef == null || playerRef.getReference() == null || !playerRef.getReference().isValid()) {
                    continue;
                }

                PlayerStats stats = new PlayerStats(playerRef);
                
                // Fetch both current and max mana
                stats.getMana().thenAccept(current -> {
                    stats.getMaxMana().thenAccept(max -> {
                        if (current >= 0) {
                            hud.updateMana(current, max);
                        }
                    });
                }).exceptionally(e -> {
                    System.err.println("[RuneCore-Manager] Error updating HUD for player: " + e.getMessage());
                    return null;
                });
            }
        }
    }
}
