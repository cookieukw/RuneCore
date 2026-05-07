package com.cookie.runecore.systems;

import com.cookie.runecore.api.CastContext;
import com.cookie.runecore.system.RuneCore;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;

public class CastListener {

    public CastListener(EventRegistry eventRegistry) {
        eventRegistry.registerGlobal(PlayerMouseButtonEvent.class, this::onMouseClick);
    }

    private void onMouseClick(PlayerMouseButtonEvent event) {
        PlayerRef playerRef = event.getPlayerRefComponent();
        if (playerRef == null)
            return;

        Item itemType = event.getItemInHand();
        if (itemType == null)
            return;

        String itemId = itemType.getId().toString();

        if (itemId.contains("stick")) {
            Ref<EntityStore> entityRef = playerRef.getReference();
            if (entityRef == null)
                return;

            Store<EntityStore> store = entityRef.getStore();
            if (store == null)
                return;

            World world = store.getExternalData() != null ? store.getExternalData().getWorld() : null;
            if (world == null)
                return;

            CastContext ctx = new CastContext(playerRef, entityRef, world, 1.0);

            RuneCore.get().castSpell("fireball", ctx);
        }
    }
}
