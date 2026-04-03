package com.cookie.runecore.systems;

import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Drives the EffectTickSystem using a Timer that fires every 50ms (~1 game tick).
 * Hooks into player join/disconnect to manage buff lifecycle.
 */
public class EffectTimerListener {

    // Map: playerUUID -> World (for safe world.execute() dispatch)
    private final ConcurrentHashMap<String, World> playerWorlds = new ConcurrentHashMap<>();
    private final Timer timer = new Timer("RuneCore-EffectTicker", true);

    public EffectTimerListener(EventRegistry eventRegistry) {
        eventRegistry.registerGlobal(AddPlayerToWorldEvent.class, this::onPlayerJoin);
        eventRegistry.registerGlobal(PlayerDisconnectEvent.class, this::onPlayerDisconnect);

        // Fire every 50ms (~20tps to match Minecraft's tick rate)
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Tick on each unique world (deduplicated)
                playerWorlds.values().stream()
                    .distinct()
                    .forEach(world -> world.execute(() -> EffectTickSystem.getInstance().tick()));
            }
        }, 50L, 50L);

        System.out.println("[RuneCore-Effects] EffectTimerListener active — 50ms tick interval");
    }

    private void onPlayerJoin(AddPlayerToWorldEvent event) {
        Holder<EntityStore> holder = event.getHolder();
        World world = event.getWorld();
        if (holder == null || world == null) return;

        // Use holder's string representation as a stable key
        String uid = holder.toString();
        playerWorlds.put(uid, world);
        System.out.println("[RuneCore-Effects] Tracking new player world entry: " + uid);
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        PlayerRef playerRef = event.getPlayerRef();
        if (playerRef == null) return;

        // Use UUID from PlayerRef.getUuid()
        String uid = playerRef.getUuid().toString();
        EffectTickSystem.getInstance().cancelAllBuffs(uid);
        playerWorlds.remove(uid);
        System.out.println("[RuneCore-Effects] Cleared buffs on disconnect: " + uid);
    }
}
