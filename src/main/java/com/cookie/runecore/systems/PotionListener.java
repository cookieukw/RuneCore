package com.cookie.runecore.systems;

import com.cookie.runecore.api.CastContext;
import com.cookie.runecore.system.RuneCore;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerInteractEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

public class PotionListener {

    private final ConcurrentHashMap<UUID, String> playerPotions;

    public PotionListener(EventRegistry eventRegistry, ConcurrentHashMap<UUID, String> playerPotions) {
        this.playerPotions = playerPotions;
        eventRegistry.registerGlobal(PlayerMouseButtonEvent.class, this::onMouseClick);
        eventRegistry.registerGlobal(PlayerInteractEvent.class, this::onPlayerInteract);
        System.out.println("[RuneCore-PotionListener] Registered global potion listeners successfully!");
    }

    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItemInHand() == null) return;
        String itemId = event.getItemInHand().getItemId();
        System.out.println("[RuneCore-PotionListener] onPlayerInteract - Item used: " + itemId);
        
        if (itemId != null && itemId.toLowerCase().contains("weapon_bomb_potion_")) {
            String lowerId = itemId.toLowerCase();
            int idx = lowerId.indexOf("weapon_bomb_potion_");
            String effectName = lowerId.substring(idx + "weapon_bomb_potion_".length());
            
            Ref<EntityStore> entityRef = event.getPlayerRef();
            if (entityRef != null && entityRef.isValid()) {
                Store<EntityStore> store = entityRef.getStore();
                if (store != null) {
                    PlayerRef playerRef = (PlayerRef) store.getComponent(entityRef, PlayerRef.getComponentType());
                    if (playerRef != null) {
                        UUID uuid = playerRef.getUuid();
                        playerPotions.put(uuid, effectName);
                        System.out.println("[RuneCore-PotionListener] Tracked potion interact: " + uuid + " -> " + effectName);
                    }
                }
            }
        } else if (itemId != null && itemId.toLowerCase().contains("potion_drinkable_")) {
            String lowerId = itemId.toLowerCase();
            int idx = lowerId.indexOf("potion_drinkable_");
            String effectName = lowerId.substring(idx + "potion_drinkable_".length());
            
            Ref<EntityStore> entityRef = event.getPlayerRef();
            if (entityRef != null && entityRef.isValid()) {
                Store<EntityStore> store = entityRef.getStore();
                if (store != null) {
                    PlayerRef playerRef = (PlayerRef) store.getComponent(entityRef, PlayerRef.getComponentType());
                    if (playerRef != null) {
                        World world = entityRef.getStore().getExternalData() != null ? 
                                      entityRef.getStore().getExternalData().getWorld() : null;

                        CastContext ctx = new CastContext(null, entityRef, world, 1.0);
                        if (RuneCore.get().getEffect(effectName) != null) {
                            RuneCore.get().getEffect(effectName).execute(ctx);
                            System.out.println("[RuneCore-PotionListener] Player drank potion, applying effect: " + effectName);
                            playerRef.sendMessage(Message.raw("[RuneCore] Você tomou a poção de: " + effectName));
                        }
                    }
                }
            }
        }
    }

    private void onMouseClick(PlayerMouseButtonEvent event) {
        if (event.getItemInHand() == null) return;
        String itemId = event.getItemInHand().getId().toString();
        System.out.println("[RuneCore-PotionListener] onMouseClick - Item used: " + itemId);
        
        if (itemId.toLowerCase().contains("weapon_bomb_potion_")) {
            String lowerId = itemId.toLowerCase();
            int idx = lowerId.indexOf("weapon_bomb_potion_");
            String effectName = lowerId.substring(idx + "weapon_bomb_potion_".length());
            
            PlayerRef playerRef = event.getPlayerRefComponent();
            if (playerRef != null) {
                playerPotions.put(playerRef.getUuid(), effectName);
                System.out.println("[RuneCore-PotionListener] Tracked potion throw: " + playerRef.getUuid() + " -> " + effectName);
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
                        System.out.println("[RuneCore-PotionListener] Player drank potion via click, applying effect: " + effectName);
                        playerRef.sendMessage(Message.raw("[RuneCore] Você tomou a poção de: " + effectName));
                    }
                }
            }
        }
    }
}
