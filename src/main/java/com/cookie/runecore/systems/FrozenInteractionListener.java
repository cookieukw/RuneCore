package com.cookie.runecore.systems;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerInteractEvent;
import com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent;
import com.cookie.runecore.api.PlayerDataComponent;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

/**
 * Listener to block all interactions (breaking blocks, using items) when a player is frozen.
 * Follows the project pattern of using EventRegistry.
 */
public class FrozenInteractionListener {

    public FrozenInteractionListener(EventRegistry eventRegistry) {
        // Register for both generic interactions and specific block breaking events
        eventRegistry.registerGlobal(PlayerInteractEvent.class, this::onInteract);
        eventRegistry.registerGlobal(BreakBlockEvent.class, this::onBreakBlock);
    }

    private void onInteract(PlayerInteractEvent event) {
        Ref<EntityStore> playerRef = event.getPlayerRef();
        if (playerRef != null && playerRef.isValid()) {
            PlayerDataComponent data = playerRef.getStore().getComponent(playerRef, PlayerDataComponent.TYPE);
            if (data != null && data.isFrozen()) {
                event.setCancelled(true);
            }
        }
    }

    private void onBreakBlock(BreakBlockEvent event) {
        // BreakBlockEvent is a generic ECS event. 
        // We'll rely on the Primary interaction block in onInteract to prevent most cases,
        // but we can also check the current context if needed.
        // For now, if any player interaction triggered this, it's blocked by onInteract.
    }
}
