package com.cookie.runecore.systems;

import com.cookie.runecore.api.CastContext;
import com.cookie.runecore.system.RuneCore;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class PotionListener {

    private static final Logger LOG = Logger.getLogger("RuneCore");

    private final ConcurrentHashMap<UUID, String> playerPotions;

    public PotionListener(EventRegistry eventRegistry, ConcurrentHashMap<UUID, String> playerPotions) {
        this.playerPotions = playerPotions;
        eventRegistry.registerGlobal(PlayerMouseButtonEvent.class, this::onMouseClick);
        // PlayerInteractEvent used to be registered here too, doing the exact same
        // tracking/casting as onMouseClick below. It is deprecated, and turned out to be dead
        // weight: nothing in HytaleServer.jar ever constructs a PlayerInteractEvent (checked by
        // scanning every class for a reference to it) — onPlayerInteract() could never have run.
        // onMouseClick, on PlayerMouseButtonEvent, is the one actually doing this job.
        LOG.fine("[RuneCore-PotionListener] Registered global potion listeners successfully!");
    }

    private void onMouseClick(PlayerMouseButtonEvent event) {
        if (event.getItemInHand() == null) return;
        String itemId = event.getItemInHand().getId().toString();
        LOG.fine("[RuneCore-PotionListener] onMouseClick - Item used: " + itemId);
        
        if (itemId.toLowerCase().contains("weapon_bomb_potion_")) {
            String lowerId = itemId.toLowerCase();
            int idx = lowerId.indexOf("weapon_bomb_potion_");
            String effectName = lowerId.substring(idx + "weapon_bomb_potion_".length());
            
            PlayerRef playerRef = event.getPlayerRefComponent();
            if (playerRef != null) {
                playerPotions.put(playerRef.getUuid(), effectName);
                LOG.fine("[RuneCore-PotionListener] Tracked potion throw: " + playerRef.getUuid() + " -> " + effectName);
            }
        } else if (itemId.toLowerCase().contains("potion_drinkable_")) {
            String lowerId = itemId.toLowerCase();
            int idx = lowerId.indexOf("potion_drinkable_");
            String effectName = lowerId.substring(idx + "potion_drinkable_".length());
            
            PlayerRef playerRef = event.getPlayerRefComponent();
            if (playerRef != null) {
                Ref<EntityStore> targetRef = playerRef.getReference();
                if (targetRef != null && targetRef.isValid()) {
                    World world = targetRef.getStore().getExternalData() != null ? 
                                  targetRef.getStore().getExternalData().getWorld() : null;

                    CastContext ctx = new CastContext(null, targetRef, world, 1.0);
                    if (RuneCore.get().getEffect(effectName) != null) {
                        RuneCore.get().getEffect(effectName).execute(ctx);
                        LOG.fine("[RuneCore-PotionListener] Player drank potion via click, applying effect: " + effectName);
                    }
                }
            }
        }
    }
}
