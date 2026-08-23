package com.cookie.runecore.systems;

import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class EffectTimerListener {

    private static final Logger LOG = Logger.getLogger("RuneCore");

    private final ConcurrentHashMap<UUID, World> playerWorlds = new ConcurrentHashMap<>();

    public EffectTimerListener(EventRegistry eventRegistry) {
        eventRegistry.registerGlobal(AddPlayerToWorldEvent.class, this::onPlayerJoin);
        eventRegistry.registerGlobal(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
        LOG.fine("[RuneCore-Effects] EffectTimerListener registered player tracking");
    }

    private void onPlayerJoin(AddPlayerToWorldEvent event) {
        World world = event.getWorld();
        Holder<EntityStore> holder = event.getHolder();
        if (world == null || holder == null) return;

        PlayerRef playerRef = holder.getComponent(PlayerRef.getComponentType());
        if (playerRef == null) return;

        UUID uid = playerRef.getUuid();
        if (uid == null) return;

        playerWorlds.put(uid, world);
        LOG.fine("[RuneCore-Effects] Player " + uid + " registered for tracking");
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        if (event.getPlayerRef() == null) return;

        UUID uid = event.getPlayerRef().getUuid();
        if (uid == null) return;

        playerWorlds.remove(uid);
    }
}
