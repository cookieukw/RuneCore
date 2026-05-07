package com.cookie.runecore.systems;

import com.cookie.runecore.api.CastContext;
import com.cookie.runecore.system.RuneCore;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.component.SystemGroup;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.event.events.player.PlayerMouseButtonEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

import com.hypixel.hytale.server.core.event.events.player.PlayerInteractEvent;

public class PotionHitSystem extends DamageEventSystem {

    private final ConcurrentHashMap<UUID, String> playerPotions = new ConcurrentHashMap<>();

    public PotionHitSystem(EventRegistry eventRegistry) {
        eventRegistry.registerGlobal(PlayerMouseButtonEvent.class, this::onMouseClick);
        eventRegistry.registerGlobal(PlayerInteractEvent.class, this::onPlayerInteract);
    }

    private void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItemInHand() == null) return;
        String itemId = event.getItemInHand().getItemId();
        System.out.println("[PotionHitSystem] onPlayerInteract - Item used: " + itemId);
        
        if (itemId != null && itemId.toLowerCase().contains("weapon_bomb_potion_")) {
            String lowerId = itemId.toLowerCase();
            int idx = lowerId.indexOf("weapon_bomb_potion_");
            String effectName = lowerId.substring(idx + "weapon_bomb_potion_".length());
            
            if (event.getPlayer() != null && event.getPlayer().getPlayerRef() != null) {
                UUID uuid = event.getPlayer().getPlayerRef().getUuid();
                playerPotions.put(uuid, effectName);
                System.out.println("[PotionHitSystem] Tracked potion interact: " + uuid + " -> " + effectName);
            }
        }
    }

    private void onMouseClick(PlayerMouseButtonEvent event) {
        if (event.getItemInHand() == null) return;
        String itemId = event.getItemInHand().getId().toString();
        System.out.println("[PotionHitSystem] onMouseClick - Item used: " + itemId);
        
        if (itemId.toLowerCase().contains("weapon_bomb_potion_")) {
            // Find effect name: weapon_bomb_potion_regeneration -> regeneration
            String lowerId = itemId.toLowerCase();
            int idx = lowerId.indexOf("weapon_bomb_potion_");
            String effectName = lowerId.substring(idx + "weapon_bomb_potion_".length());
            
            PlayerRef playerRef = event.getPlayerRefComponent();
            if (playerRef != null) {
                playerPotions.put(playerRef.getUuid(), effectName);
                System.out.println("[PotionHitSystem] Tracked potion throw: " + playerRef.getUuid() + " -> " + effectName);
            }
        }
    }

    @Override
    public SystemGroup<EntityStore> getGroup() {
        return DamageModule.get().getInspectDamageGroup();
    }

    @Override
    public Query<EntityStore> getQuery() {
        return Query.and(
            TransformComponent.getComponentType()
        );
    }

    @Override
    public void handle(int index, ArchetypeChunk<EntityStore> chunk, Store<EntityStore> store,
                       CommandBuffer<EntityStore> commandBuffer, Damage damage) {
        
        Damage.Source source = damage.getSource();
        if (source == null) return;
        
        System.out.println("[PotionHitSystem] handle() damage event detected! Source class: " + source.getClass().getName() + " amount: " + damage.getAmount());
        
        if (source instanceof Damage.ProjectileSource projSource) {
            Ref<EntityStore> attackerRef = projSource.getRef();
            if (attackerRef == null) return;
            
            PlayerRef playerRef = store.getComponent(attackerRef, PlayerRef.getComponentType());
            if (playerRef == null) return;
            
            String effectName = playerPotions.get(playerRef.getUuid());
            if (effectName == null) return;
            
            Ref<EntityStore> targetRef = chunk.getReferenceTo(index);
            if (targetRef != null) {
                World world = targetRef.getStore().getExternalData() != null ? 
                              targetRef.getStore().getExternalData().getWorld() : null;

                CastContext ctx = new CastContext(null, targetRef, world, 1.0);
                
                if (RuneCore.get().getEffect(effectName) != null) {
                    RuneCore.get().getEffect(effectName).execute(ctx);
                    System.out.println("[PotionHitSystem] Applying tracked effect: " + effectName + " to target!");
                } else {
                    System.err.println("[PotionHitSystem] Tracked effect not found: " + effectName);
                }
            }
        }
    }
}
