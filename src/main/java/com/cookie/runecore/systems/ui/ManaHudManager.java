package com.cookie.runecore.systems.ui;

import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.system.ISystem;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.event.EventRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

public class ManaHudManager {

    private final Map<UUID, ManaHud> activeHuds = new HashMap<>();

    public ManaHudManager(EventRegistry eventRegistry) {
        eventRegistry.register(PlayerConnectEvent.class, this::onPlayerConnect);
        eventRegistry.register(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
    }

    private void onPlayerConnect(PlayerConnectEvent event) {
        PlayerRef playerRef = event.getPlayerRef();
        ManaHud hud = new ManaHud(playerRef);
        activeHuds.put(playerRef.getUuid(), hud);
        
        // Show the HUD
        hud.show();
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        activeHuds.remove(event.getPlayerRef().getUuid());
    }
}
