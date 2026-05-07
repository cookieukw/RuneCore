package com.cookie.runecore.systems;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EffectTimerListener {

    private final ConcurrentHashMap<UUID, World> playerWorlds = new ConcurrentHashMap<>();
    private final Timer timer = new Timer("RuneCore-EffectTicker", true);

    public EffectTimerListener(EventRegistry eventRegistry) {
        eventRegistry.registerGlobal(AddPlayerToWorldEvent.class, this::onPlayerJoin);
        eventRegistry.registerGlobal(PlayerDisconnectEvent.class, this::onPlayerDisconnect);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                playerWorlds.values().stream()
                    .distinct()
                    .filter(w -> w != null && w.isAlive())
                    .forEach(world -> world.execute(() -> EffectTickSystem.getInstance().tick(world)));
            }
        }, 50L, 50L);

        System.out.println("[RuneCore-Effects] EffectTimerListener active — 50ms tick interval");
    }

    private void onPlayerJoin(AddPlayerToWorldEvent event) {
        World world = event.getWorld();
        if (world == null || event.getHolder() == null) return;

        // Retrieve the PlayerRef component from the Holder to get the canonical UUID.
        // The Holder is the entity snapshot — PlayerRef is stored as a component inside it.
        PlayerRef playerRef = event.getHolder().getComponent(PlayerRef.getComponentType());
        if (playerRef == null) return;

        UUID uid = playerRef.getUuid();
        if (uid == null) return;

        playerWorlds.put(uid, world);
        System.out.println("[RuneCore-Effects] Player " + uid + " registered for effect ticking (worlds tracked: " + playerWorlds.size() + ")");
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        if (event.getPlayerRef() == null) return;

        UUID uid = event.getPlayerRef().getUuid();
        if (uid == null) return;

        EffectTickSystem.getInstance().cancelAllBuffs(uid.toString());
        playerWorlds.remove(uid);
    }
}
