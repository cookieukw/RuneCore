package com.cookie.runecore.systems;

import com.cookie.runecore.api.ActiveBuff;
import com.cookie.runecore.system.RuneCore;
import com.hypixel.hytale.component.Holder;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.server.core.asset.type.entityeffect.config.EntityEffect;
import com.hypixel.hytale.server.core.entity.effect.ActiveEntityEffect;
import com.hypixel.hytale.server.core.entity.effect.EffectControllerComponent;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EffectTimerListener {

    private final ConcurrentHashMap<UUID, World> playerWorlds = new ConcurrentHashMap<>();

    public EffectTimerListener(EventRegistry eventRegistry) {
        eventRegistry.registerGlobal(AddPlayerToWorldEvent.class, this::onPlayerJoin);
        eventRegistry.registerGlobal(PlayerDisconnectEvent.class, this::onPlayerDisconnect);
        System.out.println("[RuneCore-Effects] EffectTimerListener registered player tracking");
    }

    private void onPlayerJoin(AddPlayerToWorldEvent event) {
        World world = event.getWorld();
        Holder<EntityStore> holder = event.getHolder();
        if (world == null || holder == null) return;

        PlayerRef playerRef = holder.getComponent(PlayerRef.getComponentType());
        if (playerRef == null) return;

        UUID uid = playerRef.getUuid();
        if (uid == null) return;

        playerWorlds.put(uid, world);
        System.out.println("[RuneCore-Effects] Player " + uid + " registered for tracking");

        // Restore saved active buffs from EffectControllerComponent
        EffectControllerComponent controller = holder.getComponent(EffectControllerComponent.getComponentType());
        if (controller != null) {
            ActiveEntityEffect[] activeEffects = controller.getAllActiveEntityEffects();
            if (activeEffects != null) {
                // Get the official Ref<EntityStore> from the world
                Ref<EntityStore> playerEntityRef = world.getEntityRef(uid);
                if (playerEntityRef != null && playerEntityRef.isValid()) {
                    for (ActiveEntityEffect activeEffect : activeEffects) {
                        if (activeEffect == null) continue;
                        int index = activeEffect.getEntityEffectIndex();
                        EntityEffect nativeEffect = EntityEffect.getAssetMap().getAsset(index);
                        if (nativeEffect != null && nativeEffect.getId() != null) {
                            String nativeId = nativeEffect.getId();
                            String effectName = nativeId.replace("runecore:", "").toLowerCase();
                            
                            // Verify if it maps to a RuneCore registered effect
                            if (RuneCore.get().getEffect(effectName) != null) {
                                int remainingTicks = (int) (activeEffect.getRemainingDuration() * 20.0f);
                                if (remainingTicks > 0) {
                                    ActiveBuff buff = ActiveBuff.builder(uid.toString(), effectName, remainingTicks).build();
                                    EffectTickSystem.getInstance().applyBuff(buff, playerEntityRef);
                                    System.out.println("[RuneCore-Effects] Restored active buff: " + effectName + " for player: " + uid + " (" + remainingTicks + " ticks remaining)");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void onPlayerDisconnect(PlayerDisconnectEvent event) {
        if (event.getPlayerRef() == null) return;

        UUID uid = event.getPlayerRef().getUuid();
        if (uid == null) return;

        EffectTickSystem.getInstance().cancelAllBuffs(uid.toString());
        playerWorlds.remove(uid);
    }
}
