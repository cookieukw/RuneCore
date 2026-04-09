package com.cookie.runecore.systems;

import com.cookie.runecore.api.CastContext;
import com.cookie.runecore.system.RuneCore;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.inventory.ItemStack;

import javax.annotation.Nonnull;

public class CastListener {

    public CastListener(EventRegistry eventRegistry) {
        eventRegistry.registerGlobal(PlayerMouseButtonEvent.class, this::onMouseClick);
    }

    private void onMouseClick(PlayerMouseButtonEvent event) {
        Player player = event.getPlayer();
        if (player == null)
            return;


        ItemStack item = player.getInventory().getItemInHand();
        if (item == null || item.getItem() == null)
            return;

        String itemId = item.getItem().getId().toString();

        if (itemId.contains("stick")) {

            PlayerRef playerRef = player.getPlayerRef();
            if (playerRef == null)
                return;

            Ref<EntityStore> ref = playerRef.getReference();
            Store<EntityStore> store = ref.getStore();
            if (store == null)
                return;

            EntityStore entityStore = store.getExternalData();
            if (entityStore == null)
                return;

            World world = entityStore.getWorld();
            if (world == null)
                return;

            Ref<EntityStore> entityRef = playerRef.getReference();
            CastContext ctx = new CastContext(playerRef, entityRef, world, 1.0);

            RuneCore.get().castSpell("fireball", ctx);
        }
    }
}
