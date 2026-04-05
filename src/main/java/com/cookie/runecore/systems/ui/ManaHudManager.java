package com.cookie.runecore.systems.ui;

import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.protocol.packets.interface_.HudComponent;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class ManaHudManager {

    private final Map<UUID, ManaHud> activeHuds = new HashMap<>();
    private final Timer updateTimer = new Timer("RuneCore-ManaHud", true);

    public ManaHudManager(EventRegistry eventRegistry) {
        eventRegistry.registerGlobal(PlayerReadyEvent.class, this::onPlayerReady);
        eventRegistry.registerGlobal(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
        
        // Mana doesn't need near-realtime updates — 500ms is plenty
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateAllHuds();
            }
        }, 1000, 500);
    }

    private void onPlayerReady(PlayerReadyEvent event) {
        Ref<EntityStore> ref = event.getPlayerRef();
        if (ref == null || !ref.isValid()) return;
        
        Player player = event.getPlayer();
        if (player == null) return;

        PlayerRef playerRef = player.getPlayerRef();
        if (playerRef == null) return;


        
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
                if (playerRef == null) continue;

                Ref<EntityStore> ref = playerRef.getReference();
                if (ref == null || !ref.isValid()) continue;

                Store<EntityStore> store = ref.getStore();
                if (store == null) continue;

                EntityStore entityStore = store.getExternalData();
                if (entityStore == null) continue;

                World world = entityStore.getWorld();
                if (world == null) continue;

                // Single world.execute() — no nested futures
                world.execute(() -> {
                    if (!ref.isValid()) return;
                    EntityStatMap statMap = (EntityStatMap) store.getComponent(ref, EntityStatMap.getComponentType());
                    if (statMap == null) return;
                    EntityStatValue manaVal = statMap.get(DefaultEntityStatTypes.getMana());
                    if (manaVal == null) return;
                    float current = manaVal.get();
                    hud.updateMana(current, 100f);
                });
            }
        }
    }
}
