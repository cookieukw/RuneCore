package com.cookie.runecore.api;


import javax.annotation.Nonnull;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class PlayerStats {
    private final Ref<EntityStore> playerRef;

    public PlayerStats(@Nonnull Ref<EntityStore> playerRef) {
        this.playerRef = playerRef;
    }

    private void modifyStat(int statId, float amount) {
        if (playerRef == null || !playerRef.isValid()) return;

        Store<EntityStore> store = playerRef.getStore();
        if (store == null) return;

        EntityStore entityStore = store.getExternalData();
        if (entityStore == null) return;
        
        World world = entityStore.getWorld();
        if (world == null) return;

        world.execute(() -> {
            EntityStatMap statMap = (EntityStatMap) store.getComponent(playerRef, EntityStatMap.getComponentType());
            if (statMap != null) {
                statMap.addStatValue(statId, amount);
            }
        });
    }
    
    private void setStat(int statId, float value) {
        if (playerRef == null || !playerRef.isValid()) return;

        Store<EntityStore> store = playerRef.getStore();
         if (store == null) return;

        EntityStore entityStore = store.getExternalData();
        if (entityStore == null) return;
        
        World world = entityStore.getWorld();
         if (world == null) return;

        world.execute(() -> {
            EntityStatMap statMap = (EntityStatMap) store.getComponent(playerRef, EntityStatMap.getComponentType());
            if (statMap != null) {
                statMap.setStatValue(statId, value);
            }
        });
    }

    public void addHealth(float amount) {
        modifyStat(DefaultEntityStatTypes.getHealth(), amount);
    }
    
    public void setHealth(float amount) {
        setStat(DefaultEntityStatTypes.getHealth(), amount);
    }

    public void addMana(float amount) {
        modifyStat(DefaultEntityStatTypes.getMana(), amount);
    }

     public void setMana(float amount) {
        setStat(DefaultEntityStatTypes.getMana(), amount);
    }

    public void addStamina(float amount) {
        modifyStat(DefaultEntityStatTypes.getStamina(), amount);
    }
    
     public void setStamina(float amount) {
        setStat(DefaultEntityStatTypes.getStamina(), amount);
    }

    // Attempting to add walk speed if possible, or leave it out for now.
    // public void addSpeed(float amount) { ... }
}
