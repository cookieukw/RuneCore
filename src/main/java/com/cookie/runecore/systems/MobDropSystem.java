package com.cookie.runecore.systems;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.ecs.DropItemEvent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.Message;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MobDropSystem extends DeathSystems.OnDeathSystem {

    @Override
    public void onComponentAdded(@Nonnull Ref<EntityStore> ref, @Nonnull DeathComponent death, @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        try {
            // Check if attacker was a player
            Damage deathInfo = death.getDeathInfo();
            if (deathInfo == null) return;

            Ref<EntityStore> attackerRef = resolveAttackerRef(deathInfo);
            if (attackerRef == null) return;
            
            // Allow drop if attacker is a player
            if (store.getComponent(attackerRef, Player.getComponentType()) == null) {
                 return;
            }
            
            // Drop Essence (Apple for now)
            ItemStack essenceItem = new ItemStack("hytale:apple", 1);
            // setLabel doesn't exist on ItemStack, using metadata or custom item later.
            // For now just plain apple.
            
            // Drop at victim location using DropItemEvent
            DropItemEvent.Drop dropEvent = new DropItemEvent.Drop(essenceItem, 0.5f);
            commandBuffer.invoke(ref, dropEvent);
             
             // Feedback to attacker (optional, for debug)
             PlayerRef attackerPlayerRef = (PlayerRef) store.getComponent(attackerRef, PlayerRef.getComponentType());
             if (attackerPlayerRef != null) {
                 attackerPlayerRef.sendMessage(Message.raw("[RuneCore] You received an Essence (Apple)!"));
             }

        } catch (Exception e) {
            System.err.println("Error in MobDropSystem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return Query.any();
    }
    
    // Helper to resolve attacker regardless of projectile or direct
    @Nullable
    private Ref<EntityStore> resolveAttackerRef(Damage damage) {
        Damage.Source source = damage.getSource();
        if (source == null) {
            return null;
        }
        // Direct entity attack
        if (source instanceof Damage.EntitySource) {
             return ((Damage.EntitySource) source).getRef();
        }
        // Projectile attack (e.g. arrow, magic)
        if (source instanceof Damage.ProjectileSource) {
            Damage.ProjectileSource ps = (Damage.ProjectileSource) source;
            return ps.getRef(); // This is usually the owner of the projectile (Shooter)
        }
        return null;
    }
}
