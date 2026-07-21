package com.cookie.runecore.systems;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EffectTimerListener {

    private final ConcurrentHashMap<UUID, World> playerWorlds = new ConcurrentHashMap<>();

    public EffectTimerListener(EventRegistry eventRegistry) {
        eventRegistry.registerGlobal(AddPlayerToWorldEvent.class, this::onPlayerJoin);
        eventRegistry.registerGlobal(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
        System.out.println("[RuneCore-Effects] EffectTimerListener registered player tracking");
    }

    private void onPlayerJoin(AddPlayerToWorldEvent event) {
        World world = event.getWorld();
        if (world == null || event.getHolder() == null) return;

        PlayerRef playerRef = event.getHolder().getComponent(PlayerRef.getComponentType());
        if (playerRef == null) return;

        UUID uid = playerRef.getUuid();
        if (uid == null) return;

        playerWorlds.put(uid, world);
        System.out.println("[RuneCore-Effects] Player " + uid + " registered for tracking");
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        if (event.getPlayerRef() == null) return;

        UUID uid = event.getPlayerRef().getUuid();
        if (uid == null) return;

        EffectTickSystem.getInstance().cancelAllBuffs(uid.toString());
        playerWorlds.remove(uid);
    }
}
