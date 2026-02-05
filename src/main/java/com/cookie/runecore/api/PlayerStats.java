package com.cookie.runecore.api;


import javax.annotation.Nonnull;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.MovementSettings;
import com.hypixel.hytale.protocol.packets.player.UpdateMovementSettings;
import com.hypixel.hytale.server.core.entity.entities.player.movement.MovementManager;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class PlayerStats {
    private final Ref<EntityStore> playerRef;

    private static final float MIN_SPEED = 0.0f;
    private static final float MAX_SPEED = 100.0f;
    private static final float DEFAULT_SPEED = 0.2f;
    private static final float MAX_STAT = 1000.0f;

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
                EntityStatValue statValue = statMap.get(statId);
                if (statValue != null) {
                    float current = statValue.get();
                    float newValue = Math.max(0, Math.min(MAX_STAT, current + amount));
                    statMap.setStatValue(statId, newValue);
                }
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
                float clampedValue = Math.max(0, Math.min(MAX_STAT, value));
                statMap.setStatValue(statId, clampedValue);
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
    
    public void subtractMana(float amount) {
        modifyStat(DefaultEntityStatTypes.getMana(), -amount);
    }

    public void addStamina(float amount) {
        modifyStat(DefaultEntityStatTypes.getStamina(), amount);
    }
    
     public void setStamina(float amount) {
        setStat(DefaultEntityStatTypes.getStamina(), amount);
    }

    public void subtractStamina(float amount) {
        modifyStat(DefaultEntityStatTypes.getStamina(), -amount);
    }

    public void subtractHealth(float amount) {
        modifyStat(DefaultEntityStatTypes.getHealth(), -amount);
    }

    private void modifySpeed(float amount) {
        if (playerRef == null || !playerRef.isValid()) return;
        Store<EntityStore> store = playerRef.getStore();
        if (store == null) return;
        EntityStore entityStore = store.getExternalData();
        if (entityStore == null) return;
        World world = entityStore.getWorld();
        if (world == null) return;

        world.execute(() -> {
            MovementManager moveManager = 
                (MovementManager) store.getComponent(playerRef, MovementManager.getComponentType());
            
            if (moveManager != null) {
                MovementSettings settings = moveManager.getSettings();
                if (settings != null) {
                    float newSpeed = settings.baseSpeed + amount;
                     float clampedSpeed = Math.max(MIN_SPEED, Math.min(MAX_SPEED, newSpeed));
                    
                    settings.baseSpeed = clampedSpeed;
                    
                    PlayerRef playerRefComp = 
                        (PlayerRef) store.getComponent(playerRef, PlayerRef.getComponentType());
                    
                    if (playerRefComp != null) {
                        PacketHandler packetHandler = playerRefComp.getPacketHandler();
                        if (packetHandler != null) {
                            packetHandler.write(new UpdateMovementSettings(settings));
                        }
                        // Feedback
                        if (newSpeed != clampedSpeed) {
                             if (newSpeed > MAX_SPEED) {
                                  playerRefComp.sendMessage(com.hypixel.hytale.server.core.Message.raw("Speed capped at " + MAX_SPEED));
                             } else if (newSpeed < MIN_SPEED) {
                                  playerRefComp.sendMessage(com.hypixel.hytale.server.core.Message.raw("Speed clamped to " + MIN_SPEED));
                             }
                        }
                    }
                }
            }
        });
    }

    private void setSpeedValue(float amount) {
        if (playerRef == null || !playerRef.isValid()) return;
        Store<EntityStore> store = playerRef.getStore();
        if (store == null) return;
        EntityStore entityStore = store.getExternalData();
        if (entityStore == null) return;
        World world = entityStore.getWorld();
        if (world == null) return;

        world.execute(() -> {
             MovementManager moveManager = 
                (MovementManager) store.getComponent(playerRef, MovementManager.getComponentType());
            
            if (moveManager != null) {
                MovementSettings settings = moveManager.getSettings();
                if (settings != null) {
                    float clampedSpeed = Math.max(MIN_SPEED, Math.min(MAX_SPEED, amount));
                    settings.baseSpeed = clampedSpeed;

                    PlayerRef playerRefComp = 
                        (PlayerRef) store.getComponent(playerRef, PlayerRef.getComponentType());
                    
                    if (playerRefComp != null) {
                        PacketHandler packetHandler = playerRefComp.getPacketHandler();
                        if (packetHandler != null) {
                            packetHandler.write(new UpdateMovementSettings(settings));
                        }
                         // Feedback
                        if (amount != clampedSpeed) {
                             if (amount > MAX_SPEED) {
                                  playerRefComp.sendMessage(com.hypixel.hytale.server.core.Message.raw("Speed capped at " + MAX_SPEED));
                             } else if (amount < MIN_SPEED) {
                                  playerRefComp.sendMessage(com.hypixel.hytale.server.core.Message.raw("Speed clamped to " + MIN_SPEED));
                             }
                        }
                    }
                }
            }
        });
    }

    public void addSpeed(float amount) {
        modifySpeed(amount);
    }

    public void setSpeed(float amount) {
        setSpeedValue(amount);
    }

    public void subtractSpeed(float amount) {
        modifySpeed(-amount);
    }
    
    public void resetSpeed() {
        setSpeedValue(DEFAULT_SPEED);
    }


}
