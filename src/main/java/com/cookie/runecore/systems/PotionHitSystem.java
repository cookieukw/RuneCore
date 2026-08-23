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

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.event.events.player.PlayerInteractEvent;
import java.util.logging.Logger;

public class PotionHitSystem extends DamageEventSystem {

    private static final Logger LOG = Logger.getLogger("RuneCore");

    private final ConcurrentHashMap<UUID, String> playerPotions = new ConcurrentHashMap<>();

    public PotionHitSystem() {
    }

    public ConcurrentHashMap<UUID, String> getPlayerPotions() {
        return this.playerPotions;
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
        
        LOG.fine("[PotionHitSystem] handle() damage event detected! Source class: " + source.getClass().getName() + " amount: " + damage.getAmount());
        
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
                    LOG.fine("[PotionHitSystem] Applying tracked effect: " + effectName + " to target!");
                } else {
                    LOG.warning("[PotionHitSystem] Tracked effect not found: " + effectName);
                }
            }
        }
    }
}
