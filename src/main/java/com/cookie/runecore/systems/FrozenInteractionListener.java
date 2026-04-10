package com.cookie.runecore.systems;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;
import com.cookie.runecore.api.PlayerDataComponent;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

/**
 * Listener to block all interactions (using items, clicking) when a player is frozen.
 * Uses PlayerMouseButtonEvent (replaces deprecated PlayerInteractEvent).
 * Note: BreakBlockEvent is an ECS event with no player ref and cannot be used here.
 */
public class FrozenInteractionListener {

    public FrozenInteractionListener(EventRegistry eventRegistry) {
        eventRegistry.registerGlobal(PlayerMouseButtonEvent.class, this::onMouseButton);
    }

    private void onMouseButton(PlayerMouseButtonEvent event) {
        Ref<EntityStore> playerRef = event.getPlayerRef();
        if (playerRef != null && playerRef.isValid()) {
            PlayerDataComponent data = playerRef.getStore().getComponent(playerRef, PlayerDataComponent.TYPE);
            if (data != null && data.isFrozen()) {
                event.setCancelled(true);
            }
        }
    }
}
